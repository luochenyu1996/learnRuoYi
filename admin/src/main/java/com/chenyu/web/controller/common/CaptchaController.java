package com.chenyu.web.controller.common;

import com.chenyu.common.constant.Constants;
import com.chenyu.common.core.domain.AjaxResult;
import com.chenyu.common.core.redis.RedisCache;
import com.chenyu.common.utils.uuid.IdUtils;
import com.chenyu.system.service.ISysConfigService;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chenyu.common.utils.sign.Base64;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @program: learnRuoYi
 * @description: 获取验证码
 * @author: chen yu
 * @create: 2021-10-03 10:02
 */
@Slf4j
@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    // 配置文件读取验证码类型 有 "math" 和 "char" 两种
    @Value("${ruoyi.captchaType}")
    private String captchaType;

    @Autowired
    private ISysConfigService sysConfigService;


    /**
     * 获取验证码
     *
     * @return 请求结果
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) {
        AjaxResult ajaxResult = AjaxResult.success();
        boolean captchaOnOff = sysConfigService.selectCaptchaOnOff();
        ajaxResult.put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) { //如果验证码没有打开则直接进行返回
            return ajaxResult;
        }

        //生成验证码 这里有两种验证码  图片和数字  两种类型
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;  //验证码在redis中的Key
        //验证码表达式
        String capStr = null;
        //验证码答案
        String code = null;
        BufferedImage image = null;

        if ("math".equals(captchaType)) {//数学类型
            //生成了带答案的 验证码表达式 例如： capText:2*1=?@2
            String capText = captchaProducerMath.createText();
            log.info("生成的验证码，未处理前:{}", capText);
            //截取了原始验证码表达式中 @前面的内容，也就是 ：cptStr:2*1=?
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            //取出验证码的答案  code:2
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        //对验证码进行缓存
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        //转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        //生成验证码时候的UUID 要返回给前端
        ajaxResult.put("uuid", uuid);
        ajaxResult.put("img", Base64.encode(os.toByteArray()));
        return ajaxResult;
    }
}