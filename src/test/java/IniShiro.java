import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniShiro {
    // classpath是一个单词 不要写成classPath
    IniRealm iniRealm = new IniRealm("classpath:shiro-ini.ini");

    @Test
    public void testAuthentication(){

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        // 2.subject(主体，正文)提交认证请求
        // 似乎是配置运行环境
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 获取主体
        Subject subject = SecurityUtils.getSubject();
        // 登录方法
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
        subject.login(token);

        // 是否认证成功方法
        boolean flag = subject.isAuthenticated();
        if (flag){
            System.out.println("登录成功");
        } else {
            System.out.println("登录失败");
        }
        // 验证是否具有权限(用户角色)
        subject.checkRole("admin");
        // 验证是否具有权限 （这个似乎不能一次验证多个权限啊）
        subject.checkPermission("user:delete");
    }
}
