/*
 * The MIT License
 * 
 * Copyright (c) 2013 IKEDA Yasuyuki
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jp.ikedam.jenkins.plugins.disablepopup;

import static org.junit.Assert.*;
import hudson.model.User;
import jenkins.model.Jenkins;
import jp.ikedam.jenkins.plugins.disablepopup.DisablePopupUserProperty.DescriptorImpl;
import jp.ikedam.jenkins.plugins.disablepopup.DisablePopupUserProperty.UserDisablePopupConf;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule.WebClient;
import org.jvnet.hudson.test.recipes.LocalData;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 *
 */
public class DisablePopupUserPropertyTest
{
    @Rule
    public DisablePopupJenkinsRule j = new DisablePopupJenkinsRule();
    
    private DescriptorImpl getDescriptor()
    {
        return (DescriptorImpl)j.jenkins.getDescriptor(DisablePopupUserProperty.class);
    }
    
    @Test
    public void testDisablePopupUserProperty() throws Exception
    {
        {
            DisablePopupUserProperty target = new DisablePopupUserProperty(UserDisablePopupConf.NOCONF);
            assertEquals(UserDisablePopupConf.NOCONF, target.getDisablePopup());
        }
        {
            DisablePopupUserProperty target = new DisablePopupUserProperty(UserDisablePopupConf.TRUE);
            assertEquals(UserDisablePopupConf.TRUE, target.getDisablePopup());
        }
        {
            DisablePopupUserProperty target = new DisablePopupUserProperty(UserDisablePopupConf.FALSE);
            assertEquals(UserDisablePopupConf.FALSE, target.getDisablePopup());
        }
    }
    
    @Test
    public void testDescriptor_newInstance() throws Exception
    {
        DisablePopupUserProperty target = (DisablePopupUserProperty)getDescriptor().newInstance((User)null);
        assertNotNull(target);
        assertEquals(UserDisablePopupConf.NOCONF, target.getDisablePopup());
    }
    
    @Test
    public void testGetDisablePopupForUser() throws Exception
    {
        {
            assertEquals(UserDisablePopupConf.NOCONF, DisablePopupUserProperty.getDisablePopupForUser(null));
        }
        /*
        // UserProperties are automatically initialized, so there is no way to test this pattern.
        {
            User user = Jenkins.getInstance().getUser("test1");
            assertNull(user.getProperty(DisablePopupUserProperty.class));
            assertEquals(UserDisablePopupConf.NOCONF, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
        */
        {
            User user = Jenkins.getInstance().getUser("test2");
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.NOCONF));
            assertEquals(UserDisablePopupConf.NOCONF, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
        {
            User user = Jenkins.getInstance().getUser("test3");
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.TRUE));
            assertEquals(UserDisablePopupConf.TRUE, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
        {
            User user = Jenkins.getInstance().getUser("test4");
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.FALSE));
            assertEquals(UserDisablePopupConf.FALSE, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
    }
    
    @LocalData
    @Test
    public void testConfigure() throws Exception
    {
        User user = Jenkins.getInstance().getUser("test");
        String password = "password";
        
        assertEquals(UserDisablePopupConf.NOCONF, DisablePopupUserProperty.getDisablePopupForUser(user));
        
        WebClient wc = j.createWebClient();
        wc.login(user.getId(), password);
        
        {
            HtmlPage page = (HtmlPage)wc.goTo(String.format("%s/configure", user.getUrl()));
            HtmlForm configForm = page.getFormByName("config");
            HtmlSelect sel = configForm.getSelectByName("disablePopup");
            sel.setSelectedAttribute(UserDisablePopupConf.TRUE.name(), true);
            j.submit(configForm);
            assertNotNull(user.getProperty(DisablePopupUserProperty.class));
            assertEquals(UserDisablePopupConf.TRUE, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
        {
            HtmlPage page = (HtmlPage)wc.goTo(String.format("%s/configure", user.getUrl()));
            HtmlForm configForm = page.getFormByName("config");
            HtmlSelect sel = configForm.getSelectByName("disablePopup");
            sel.setSelectedAttribute(UserDisablePopupConf.FALSE.name(), true);
            j.submit(configForm);
            assertNotNull(user.getProperty(DisablePopupUserProperty.class));
            assertEquals(UserDisablePopupConf.FALSE, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
        {
            HtmlPage page = (HtmlPage)wc.goTo(String.format("%s/configure", user.getUrl()));
            HtmlForm configForm = page.getFormByName("config");
            HtmlSelect sel = configForm.getSelectByName("disablePopup");
            sel.setSelectedAttribute(UserDisablePopupConf.NOCONF.name(), true);
            j.submit(configForm);
            assertNotNull(user.getProperty(DisablePopupUserProperty.class));
            assertEquals(UserDisablePopupConf.NOCONF, DisablePopupUserProperty.getDisablePopupForUser(user));
        }
   }
}
