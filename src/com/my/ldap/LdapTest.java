package com.my.ldap;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Java通过Ldap操作AD的增删该查询
 * 
 * @author guob
 */

public class LdapTest {
    DirContext dc = null;
    String root = "dc=cmcc"; // LDAP的根节点的DC

    /**
     * 
     * @param dn类似于"CN=RyanHanson,dc=example,dc=com"
     * @param employeeID是Ad的一个员工号属性
     */
    public LdapTest(String dn, String employeeID) {
        init();
        // add();//添加节点
        // delete("ou=hi,dc=example,dc=com");//删除"ou=hi,dc=example,dc=com"节点
        // renameEntry("ou=new,o=neworganization,dc=example,dc=com","ou=neworganizationalUnit,o=neworganization,dc=example,dc=com");//重命名节点"ou=new,o=neworganization,dc=example,dc=com"
         searchInformation("ou=pwdPolicy,ou=policy,dc=organization,dc=cmcc", "", "(|(pwdPolicyType= 0)(&(pwdPolicyType= 1)(objectclass=top)(objectclass=cmcc-baseBusinessObj)(objectclass=cmcc-pwdPolicy)))");//遍历所有根节点
        //modifyInformation(dn, employeeID);// 修改
        // Ldapbyuserinfo("guob");//遍历指定节点的分节点
        close();
    }

    /**
     * 
     * Ldap连接
     * 
     * @return LdapContext
     */
    public void init() {
        Hashtable env = new Hashtable();
        String LDAP_URL = "ldap://127.0.0.1:61389"; // LDAP访问地址
        String adminName = "cn=Directory Manager"; // 注意用户名的写法：domain\User或
        String adminPassword = "2016ultra4A"; // 密码
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
        try {
            dc = new InitialDirContext(env);// 初始化上下文
            System.out.println("认证成功");// 这里可以改成异常抛出。
        } catch (javax.naming.AuthenticationException e) {
            System.out.println("认证失败");
        } catch (Exception e) {
            System.out.println("认证出错：" + e);
        }
    }

    /**
     * 添加
     */
    public void add(String newUserName) {
        try {
            BasicAttributes attrs = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute("objectClass");
            objclassSet.add("sAMAccountName");
            objclassSet.add("employeeID");
            attrs.put(objclassSet);
            attrs.put("ou", newUserName);
            dc.createSubcontext("ou=" + newUserName + "," + root, attrs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in add():" + e);
        }
    }

    /**
     * 删除
     * 
     * @param dn
     */
    public void delete(String dn) {
        try {
            dc.destroySubcontext(dn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in delete():" + e);
        }
    }

    /**
     * 重命名节点
     * 
     * @param oldDN
     * @param newDN
     * @return
     */
    public boolean renameEntry(String oldDN, String newDN) {
        try {
            dc.rename(oldDN, newDN);
            return true;
        } catch (NamingException ne) {
            System.err.println("Error: " + ne.getMessage());
            return false;
        }
    }

//    /**
//     * 修改
//     * 
//     * @return
//     */
//    public boolean modifyInformation(String dn, String employeeID) {
//        try {
//            System.out.println("updating...\n");
//            ModificationItem[] mods = new ModificationItem[1];
//            /* 修改属性 */
//            // Attribute attr0 = new BasicAttribute("employeeID", "W20110972");
//            // mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
//            // attr0);
//            /* 删除属性 */
//            // Attribute attr0 = new BasicAttribute("description",
//            // "陈轶");
//            // mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
//            // attr0);
//            /* 添加属性 */
//            Attribute attr0 = new BasicAttribute("employeeID", employeeID);
//            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr0);
//            /* 修改属性 */
//            dc.modifyAttributes(dn + ",dc=example,dc=com", mods);
//            return true;
//        } catch (NamingException e) {
//            e.printStackTrace();
//            System.err.println("Error: " + e.getMessage());
//            return false;
//        }
//    }

    /**
     * 关闭Ldap连接
     */
    public void close() {
        if (dc != null) {
            try {
                dc.close();
            } catch (NamingException e) {
                System.out.println("NamingException in close():" + e);
            }
        }
    }

    /**
     * @param base：根节点(在这里是"dc=example,dc=com")
     * @param scope：搜索范围,分为"base"(本节点),"one"(单层),""(遍历)
     * @param filter：指定子节点(格式为"(objectclass=*)",*是指全部，你也可以指定某一特定类型的树节点)
     */
    public void searchInformation(String base, String scope, String filter) {
        SearchControls sc = new SearchControls();
        if (scope.equals("base")) {
            sc.setSearchScope(SearchControls.OBJECT_SCOPE);
        } else if (scope.equals("one")) {
            sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        } else {
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        }
        
        List<String> sqllist = new ArrayList<>();
        NamingEnumeration ne = null;
        try {
            FileWriter writer = new FileWriter("d:\\test.sql");
            ne = dc.search(base, filter, sc);
            while (ne.hasMore()) {
                System.out.println();
                SearchResult sr = (SearchResult) ne.next();
                String name = sr.getName();
                if (base != null && !base.equals("")) {
                    System.out.println("entry: " + name + "," + base);
                } else {
                    System.out.println("entry: " + name);
                }

                StringBuilder sql = new StringBuilder();
                Attributes at = sr.getAttributes();
                NamingEnumeration ane = at.getAll();
                while (ane.hasMore()) {
                    Attribute attr = (Attribute) ane.next();
                    String attrType = attr.getID();
                    NamingEnumeration values = attr.getAll();
                    Vector vals = new Vector();
                    while (values.hasMore()) {
                        Object oneVal = values.nextElement();
                        if("fixedPWd".contains(attrType)) {
                            System.out.println(attrType+"-------"+oneVal);
                        }
                    }
                }
            }
            writer.close();
        } catch (Exception nex) {
            System.err.println("Error: " + nex.getMessage());
            nex.printStackTrace();
        }
    }

    /**
     * 查询
     * 
     * @throws NamingException
     */
    public void Ldapbyuserinfo(String userName) {
        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        // specify the LDAP search filter
        String searchFilter = "sAMAccountName=" + userName;
        // Specify the Base for the search 搜索域节点
        String searchBase = "DC=example,DC=COM";
        int totalResults = 0;
        String returnedAtts[] = { "url", "whenChanged", "employeeID", "name", "userPrincipalName", "physicalDeliveryOfficeName",
                "departmentNumber", "telephoneNumber", "homePhone", "mobile", "department", "sAMAccountName", "whenChanged", "mail" }; // 定制返回属性

        searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集

        try {
            NamingEnumeration answer = dc.search(searchBase, searchFilter, searchCtls);
            if (answer == null || answer.equals(null)) {
                System.out.println("answer is null");
            } else {
                System.out.println("answer not null");
            }
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                System.out.println("************************************************");
                System.out.println("getname=" + sr.getName());
                Attributes Attrs = sr.getAttributes();
                if (Attrs != null) {
                    try {

                        for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
                            Attribute Attr = (Attribute) ne.next();
                            System.out.println("AttributeID=" + Attr.getID().toString());
                            // 读取属性值
                            for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
                                String user = e.next().toString(); // 接受循环遍历读取的userPrincipalName用户属性
                                System.out.println(user);
                            }
                        }
                    } catch (NamingException e) {
                        System.err.println("Throw Exception : " + e);
                    }
                }
            }
            System.out.println("Number: " + totalResults);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
    }

    /**
     * 主函数用于测试
     * 
     * @param args
     */
    public static void main(String[] args) {
        new LdapTest("CN=RyanHanson", "bbs.it-home.org");
    }
}
