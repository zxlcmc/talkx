package org.bigmouth.gpt.mapper.talkx;

import org.bigmouth.gpt.entity.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.bigmouth.gpt.entity.FriendTagCount;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2023-12-13
 */
public interface FriendMapper extends BaseMapper<Friend> {

    List<FriendTagCount> countFriendTag();
}
