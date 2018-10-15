import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class ShiroTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();


    @Test
    public void testAuthentication(){
        // ...roles 为权限设置
        simpleAccountRealm.addAccount("admin", "123456","admin");
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);
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
        // 验证是否具有权限
        subject.checkRole("admin") ;
    }
}
