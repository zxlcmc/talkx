package org.bigmouth.gpt.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * @author allen
 * @date 2017-11-27
 */
@ConfigurationProperties("config.jedis")
public class JedisConfiguration extends JedisPoolConfig {

    private String host = Protocol.DEFAULT_HOST;
    private String password;
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private int database = Protocol.DEFAULT_DATABASE;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}
