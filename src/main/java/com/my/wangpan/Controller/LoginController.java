package com.my.wangpan.Controller;

import com.my.wangpan.Utils.MailUtil;
import com.my.wangpan.generator.domain.FileFolder;
import com.my.wangpan.generator.domain.FileStore;
import com.my.wangpan.generator.domain.User;
import com.my.wangpan.generator.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class LoginController extends BaseController{

    @Autowired
    UserService userService;

    /**
     * 注册邮箱发送验证码
     * @param email
     * @param userName
     * @param password
     * @return
     */
    @ResponseBody
    @GetMapping("/sendCode")
    public String sendCode(String email,String userName,String password){
        /**
         * 验证邮箱格式是否正确
         * 不正确返回
         * 生成验证码，发送到邮箱
         * 验证是否发送成功
         */
        //验证邮箱是否被注册
        User byEmail = userService.getByEmail(email);
        if(byEmail!=null){
            log.info("邮箱已经被注册！发送验证码失败");
            return "exitEmail";
        }
        //发送邮件
        log.info("开始发送邮件...\n"+"邮箱发送对象为"+mailSender);
        mailUtil = new MailUtil(mailSender);
        String code = mailUtil.sendCodeEmail(email, userName, password);
        session.setAttribute(email+"_code",code);
        return "success";
    }

    /**
     * 注册用户
     * @param user
     * @param code
     * @param map
     * @return
     */
    @PostMapping("/register")
    public String register(User user, String code, Map<String, String> map){
        /**
         * 参数校验
         * 对比验证码
         * 如果验证码错误，注册失败
         * 如果验证码正确，注册成功，跳转主界面
         */
        String ucode = (String) session.getAttribute(user.getEmail()+"_code");
        if(!ucode.equals(code)){
            String error = "验证码错误";
            map.put("errorMsg",error);
            return "index";
        }
        //用户名区空格
        user.setUserName(user.getUserName().trim());
        user.setImagePath("https://assets.leetcode.cn/aliyun-lc-upload/users/lao-song-2f/avatar_1663378324.png?x-oss-process=image%2Fformat%2Cwebp");
        user.setRegisterTime(new Date());
        user.setRole(1);
        if(userService.save(user)){
            //给user设置存储仓库
            FileStore store = FileStore.builder().userId(user.getUserId()).currentSize(0).build();
            fileStoreService.save(store);
            user.setFileStoreId(store.getFileStoreId());
            userService.updateById(user);
            log.info("注册用户成功！当前用户为："+user);
            log.info("注册仓库成功！当前仓库为："+store);
        }else {
            map.put("errorMsg", "服务器发生错误，注册失败");
            return "index";
        }
        session.removeAttribute(user.getEmail() + "_code");
        session.setAttribute("loginUser", user);
        return "redirect:/index";
    }

    /**
     * 处理登录请求
     * @param email
     * @param password
     * @param map
     * @return
     */
    @PostMapping("/login")
    public String Login(String email,String password,Map<String,Object> map){
        User user = userService.getByEmail(email);
        if(user!=null && user.getPassword().equals(password)){
            //登陆成功
            session.setAttribute("loginUser",user);
            log.info("登陆成功："+user);
            return "redirect:/index";
        }else {
            String errMsg = user == null ? "该邮箱未注册" : "密码错误";
            log.info("登陆失败");
            map.put("errorMsg",errMsg);
            return "index";
        }
    }

    /**
     * 退出登录，清空session
     * @return
     */
    @GetMapping("/logout")
    public String logout(){
        log.info("用户退出登录！"+loginUser.getUserName());
        session.invalidate();
        return "redirect:/";
    }
}
