package org.bigmouth.gpt.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.bigmouth.gpt.entity.AigcImages;
import org.bigmouth.gpt.entity.PageRequest;
import org.bigmouth.gpt.entity.PageVo;
import org.bigmouth.gpt.entity.Session;
import org.bigmouth.gpt.service.IAigcImagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author allen
 * @since 2023-12-06
 */
@RestController
@RequestMapping("/aigc_images")
public class AigcImagesController {

    private final IAigcImagesService aigcImagesService;

    public AigcImagesController(IAigcImagesService aigcImagesService) {
        this.aigcImagesService = aigcImagesService;
    }

    @GetMapping("/list")
    public ResponseEntity<PageVo<AigcImages>> list(@Validated ListRequest request) {
        IPage<AigcImages> page = new Page<AigcImages>()
                .setCurrent(request.getCurrent())
                .setSize(request.getSize());
        IPage<AigcImages> result = aigcImagesService.page(page, Wrappers.query(new AigcImages()).orderByDesc(Session.ID));
        PageVo<AigcImages> aigcImagesPageVo = new PageVo<>(result);
        return ResponseEntity.ok(aigcImagesPageVo);
    }

    @Data
    public static class ListRequest extends PageRequest {}
}
