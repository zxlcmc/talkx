package org.bigmouth.gpt.xiaozhi.udp;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiConfig;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiUdpConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class NettyUDPServer implements DisposableBean {

    private final NettyChannelInboundHandler channelInboundHandler;
    private final ClientAddressHolder clientAddressHolder;
    private EventLoopGroup group;
    private Channel channel;
    private LoadingCache<String, Optional<ClientAddress>> loadingCache = CacheBuilder.newBuilder()
            .initialCapacity(200)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Optional<ClientAddress>>() {
                @Override
                public Optional<ClientAddress> load(String key) throws Exception {
                    return Optional.ofNullable(clientAddressHolder.get(key));
                }
            });

    public NettyUDPServer(XiaozhiConfig xiaozhiConfig, XiaozhiUdpConfig xiaozhiUdpConfig, NettyChannelInboundHandler channelInboundHandler, ClientAddressHolder clientAddressHolder) {
        this.channelInboundHandler = channelInboundHandler;
        this.clientAddressHolder = clientAddressHolder;
        try {
            if (xiaozhiConfig.isEnable()) {
                this.start(xiaozhiUdpConfig.getUdpServerPort());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动 UDP 服务器
     *
     * @param port 服务器监听的端口
     */
    public void start(int port) throws InterruptedException {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).handler(channelInboundHandler);

        // 设置 channelInboundHandler 的 NettyUDPServer 属性
        channelInboundHandler.setNettyUDPServer(this);

        // 绑定端口并启动服务器
        ChannelFuture future = bootstrap.bind(port).sync();
        channel = future.channel();
        log.info("UDP Server started on port: {}", port);
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    /**
     * 关闭 UDP 服务器
     */
    public void stop() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        log.info("UDP Server stopped");
    }

    /**
     * 向客户端发送消息
     *
     * @param data 要发送的消息
     * @param address 客户端的地址
     * @param port    客户端的端口
     */
    public void sendMessage(byte[] data, String address, int port) {
        if (channel == null || !channel.isActive()) {
            log.warn("Server is not running!");
            return;
        }
        InetSocketAddress clientAddress = new InetSocketAddress(address, port);
        DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(data), clientAddress);

        channel.writeAndFlush(packet).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("Message sent to " + clientAddress);
                }
            } else {
                log.info("Failed to send message: " + future.cause());
            }
        });
    }

    /**
     * 向客户端发送消息
     * @param data 要发送的消息
     * @param sessionId sessionId
     */
    public void sendMessage(byte[] data, String sessionId) {
        try {
            Optional<ClientAddress> optional = loadingCache.get(sessionId);
            if (!optional.isPresent()) {
                log.warn("Client address not found for sessionId: {}", sessionId);
                return;
            }
            ClientAddress clientAddress = optional.get();
            sendMessage(data, clientAddress.getIp(), clientAddress.getPort());
        } catch (ExecutionException e) {
            log.error("Error sending message: ", e);
        }
    }

    public void loadClientAddressCache(String sessionId) {
        loadingCache.refresh(sessionId);
    }
}