package org.bigmouth.gpt.service.impl;

import org.bigmouth.gpt.entity.Favorites;
import org.bigmouth.gpt.mapper.talkx.FavoritesMapper;
import org.bigmouth.gpt.service.IFavoritesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-04-15
 */
@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements IFavoritesService {

}
