package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bxm.warcar.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.mapper.talkx.UserMapper;
import org.bigmouth.gpt.service.InviteCodeService;
import org.springframework.stereotype.Service;

/**
 * @author huxiao
 * @since 1.0.0
 */
@Slf4j
@Service
public class InviteCodeServiceImpl implements InviteCodeService {

    private static final char[] CHARS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'
    };

    private final UserMapper userMapper;


    public InviteCodeServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String getInviteCode() {
        String code;
        do {
            code = StringHelper.random(CHARS, 6);
        } while (userMapper.selectCount(Wrappers.query(new User().setInviteCode(code))) > 0);
        return code;
    }
}
