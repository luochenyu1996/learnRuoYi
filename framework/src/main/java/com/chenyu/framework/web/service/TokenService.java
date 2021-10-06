package com.chenyu.framework.web.service;

import com.chenyu.common.constant.Constants;
import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.core.redis.RedisCache;
import com.chenyu.common.utils.ServletUtils;
import com.chenyu.common.utils.StringUtils;
import com.chenyu.common.utils.ip.AddressUtils;
import com.chenyu.common.utils.ip.IpUtils;
import com.chenyu.common.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: learnRuoYi
 * @description: token 生成  和验证
 * @author: chen yu
 * @create: 2021-10-02 11:01
 */
@Slf4j
@Component
public class TokenService {


    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;


    /**
     * 时间上的一些转换
     */
    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;



    /**
     *  获取登录用户信息
     *
     * @param  request http请求
     * @return
     */
    public LoginUser getLoginUser(HttpServletRequest request){
        //获取请求中携带的令牌
        String token= getToken(request);
        if (StringUtils.isNotEmpty(token)){
            try {
                //解析jwt各格式的token
                Claims claims = parseToken(token);
                //解析得到这个用户的UUID
                String uuid = (String)claims.get(Constants.LOGIN_USER_KEY);
                //得到 在缓存中的Key
                String tokenRedisKey = getTokenKey(uuid);
                //缓存中取出用户信息  LoginUser
                LoginUser user = redisCache.getCacheObject(tokenRedisKey);
                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }




    /**
     * 验证token有效期  如果时间不超过20分钟 自动刷新缓存
     * 
     * @param  loginUser
     * @return  token 是否正确
     */
    
    public void verifyTokenTime(LoginUser loginUser){
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            //更新时间
            refreshToken(loginUser);
        }
    }
    







    /**
     * 生成token
     * token<==>jwt token
     * @param loginUser 系统用户
     * @return token 值
     */

    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);  //  这里 的token  是 uuid
        setUserAgent(loginUser);
        //redis 这里对用户做了存储
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(Constants.LOGIN_USER_KEY, token);//这个token 是uuid
        //生成Jwt相关的token
        return createToken(claims);
    }


    /**
     * 生成JWT格式的token
     * jwt 格式的token 包含三个部分  ：
     *  1. header :   签名算法+令牌格式，使用base64编码。
     *  2.payload:    base64编码
     *  3.signature:  使用①中指定的加密算法: signature= 加密算法{密钥+base64(head)+base64(payload)}
     *
     * @param claims 数据声名
     * @return jwt 格式的token
     */
    private String createToken(Map<String, Object> claims) {
       String token=  Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }


    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        log.info("userAgent:{}",userAgent);
        // 下方是获取用户的一些使用环境或基本的情况
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }


    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        //设置登录的时间
        loginUser.setLoginTime(System.currentTimeMillis());
        //设置有效期
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        //!!!! 存储在redis 中的并不是 返回给前端的那个jwt  token
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }


    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }


    /**
     * 获取请求中的token
     *
     * @param   request http 请求
     * @return  token
     */

    private String getToken(HttpServletRequest request){
        String token = request.getHeader(this.header);
        if (StringUtils.isNotEmpty(token)&&token.startsWith(Constants.TOKEN_PREFIX)){
            //需要把前缀删除
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }





}