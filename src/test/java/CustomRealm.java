import com.upupoo.mapper.SysUserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper sysUserMapper;
    private Map<String, String> userMap = new HashMap<>(16);

    {
        userMap.put("admin", "5393e07f94a25aaa373dbd3fa257bd3a");
    }

    /**
     * 设置realm的名称
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        super.setName("myRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        // 获取当前用户的角色身份
        Set<String> rolesSet = getRolesByUserName(username);
        // 获取当前用户的操作权限
        Set<String> permissionsSet = getPermissionByUsename(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(rolesSet);
        authorizationInfo.setStringPermissions(permissionsSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal();
        // 实际开发流程中从数据库中获取用户密码
        String password = getPasswordByUserName(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("abcdefg", password, this.getName());
        // 设置加盐策略值 Shiro提供的方法ByteSource.Util.bytes("jas")
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("abcdefg"));
        return authenticationInfo;
    }

    /**
     * 模拟从数据库中获取权限信息
     *
     * @param username
     * @return
     */
    private Set<String> getPermissionByUsename(String username) {
        Set<String> permissionsSet = new HashSet<>();
        permissionsSet.add("user:delete");
        permissionsSet.add("user:update");
        return permissionsSet;
    }

    /**
     * 模拟从从数据库中获取数据
     * @param username
     * @return
     */
    private Set<String> getRolesByUserName(String username) {
        Set<String> rolesSet = new HashSet<>();
        rolesSet.add("admin");
        rolesSet.add("user");
        return rolesSet;
    }

    /**
     * 模拟从数据库中根据用户名获取密码
     * @param username
     * @return
     */
    private String getPasswordByUserName(String username) {
        return userMap.get(username);
    }

    @Test
    public void getMD5(){
        Md5Hash md5Hash = new Md5Hash("123456", "abcdefg");
        // e10adc3949ba59abbe56e057f20f883e
        System.out.println(md5Hash);
    }
}
