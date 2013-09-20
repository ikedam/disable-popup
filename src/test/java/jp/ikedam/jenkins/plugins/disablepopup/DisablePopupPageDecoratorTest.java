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
import jp.ikedam.jenkins.plugins.disablepopup.DisablePopupUserProperty.UserDisablePopupConf;
import hudson.model.User;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule.WebClient;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *
 */
public class DisablePopupPageDecoratorTest
{
    @Rule
    public DisablePopupJenkinsRule j = new DisablePopupJenkinsRule();
    
    private DisablePopupPageDecorator getInstance()
    {
        return j.jenkins.getDescriptorByType(DisablePopupPageDecorator.class);
    }
    
    @Test
    public void testConfigure() throws Exception
    {
        DisablePopupPageDecorator target = getInstance();
        assertTrue(target.isDisablePopup());
        
        WebClient wc = j.createWebClient();
        {
            HtmlPage p = wc.goTo("configure");
            HtmlForm form = p.getFormByName("config");
            HtmlCheckBoxInput checkbox = form.getInputByName("_.disablePopup");
            assertTrue(checkbox.isChecked());
            checkbox.click();
            assertFalse(checkbox.isChecked());
            j.submit(form);
            
            assertFalse(target.isDisablePopup());
        }
        {
            HtmlPage p = wc.goTo("configure");
            HtmlForm form = p.getFormByName("config");
            HtmlCheckBoxInput checkbox = form.getInputByName("_.disablePopup");
            assertFalse(checkbox.isChecked());
            checkbox.click();
            assertTrue(checkbox.isChecked());
            j.submit(form);
            
            assertTrue(target.isDisablePopup());
        }
    }
    
    @Test
    public void testIsDisablePopupForUser() throws Exception
    {
        DisablePopupPageDecorator target = getInstance();
        assertTrue(target.isDisablePopup());
        User user = j.jenkins.getUser("test");
        
        target.setDisablePopup(true);
        {
            assertTrue(target.isDisablePopupForUser(null));
        }
        {
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.NOCONF));
            assertTrue(target.isDisablePopupForUser(user));
        }
        {
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.TRUE));
            assertTrue(target.isDisablePopupForUser(user));
        }
        {
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.FALSE));
            assertFalse(target.isDisablePopupForUser(user));
        }
        
        
        target.setDisablePopup(false);
        {
            assertFalse(target.isDisablePopupForUser(null));
        }
        {
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.NOCONF));
            assertFalse(target.isDisablePopupForUser(user));
        }
        {
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.TRUE));
            assertTrue(target.isDisablePopupForUser(user));
        }
        {
            user.addProperty(new DisablePopupUserProperty(UserDisablePopupConf.FALSE));
            assertFalse(target.isDisablePopupForUser(user));
        }
    }
}
