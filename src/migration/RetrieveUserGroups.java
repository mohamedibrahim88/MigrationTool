package migration;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

public class RetrieveUserGroups {
//
//        public static String ldapUri = "ldap://192.168.1.40:389";
//        public static String usersContainer = "CN=users,DC=filenet,DC=com";
//        public static ArrayList<String> getUserGroups (String username, String pass){
//            ArrayList<String> list = new ArrayList<String>();
//            Hashtable env = new Hashtable();
//            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//            env.put(Context.PROVIDER_URL, ldapUri);
//            env.put(Context.SECURITY_PRINCIPAL, username);
//            env.put(Context.SECURITY_CREDENTIALS, pass);
//            try {
//                DirContext ctx = new InitialDirContext(env);
//                SearchControls ctls = new SearchControls();
//                String[] attrIDs = {"cn", "memberOf"};
//                ctls.setReturningAttributes(attrIDs);
//                ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
//
//                NamingEnumeration answer = ctx.search(usersContainer, "(&(CN=" + username +"))", ctls);
//                while (answer.hasMore()) {
//                    SearchResult rslt = (SearchResult) answer.next();
//                    Attributes attrs = rslt.getAttributes();
//                    String groups = attrs.get("memberOf").toString();
//                    String[] groupname = groups.split(":");
//                    String userGroup = groupname[1];
//                    if (groups.contains("HR")){
//                        System.out.println("Member of HR group");
//                    }
//                    System.out.println(userGroup);
//                }
//                ctx.close();
//            } catch (NamingException e) {
//                e.printStackTrace();
//            }
//            return list;
//        }


    public static  ArrayList<String> GetUserGroups2(String username, String pass) {
            // LDAP server details
            String ldapUrl = "ldap://192.168.1.40:389";
            String ldapUser = "CN="+username+",CN=users,DC=filenet,DC=com"; // LDAP bind user DN
            String ldapPassword = pass; // LDAP bind user password
            String baseDn = "CN=users,DC=filenet,DC=com"; // Base DN for the search
            ArrayList<String> userGroups = new ArrayList<>();
            // User ID to retrieve groups
            String userId = username;

            // Set up the environment for creating the initial context
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
           // env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, ldapUser);
            env.put(Context.SECURITY_CREDENTIALS, ldapPassword);

        try {
            // Create the initial context
            DirContext context = new InitialDirContext(env);

            // Create the search controls
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // Set up the search filter
            String searchFilter = "(cn=" + userId + ")"; // Filter to search for the user

            // Perform the search
            NamingEnumeration<SearchResult> results = context.search(baseDn, searchFilter, searchControls);

            // Retrieve and print the groups of the user
            if (results.hasMore()) {
                SearchResult searchResult = results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute groupAttribute = attributes.get("memberOf");
                if (groupAttribute != null) {
                    NamingEnumeration<?> groupValues = groupAttribute.getAll();
                    while (groupValues.hasMore()) {
                        String groubName = groupValues.next().toString();
                        int commaIndex=  groubName.toString().indexOf(',');
                        groubName = groubName.substring(3,commaIndex);
                        userGroups.add(groubName);
                        System.out.println("Group: " + groubName);
                        System.out.println("Comma Index: " + commaIndex);
                    }
                }

            }

            // Close the context
            context.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  userGroups;
    }
    }


