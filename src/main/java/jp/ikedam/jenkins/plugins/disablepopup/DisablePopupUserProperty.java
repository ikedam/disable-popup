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

import org.jvnet.localizer.Localizable;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.model.User;

/**
 *
 */
public class DisablePopupUserProperty extends UserProperty
{
    private UserDisablePopupConf disablePopup = UserDisablePopupConf.NOCONF;
    /**
     * @return the disablePopup
     */
    public UserDisablePopupConf getDisablePopup()
    {
        return disablePopup;
    }
    
    @DataBoundConstructor
    public DisablePopupUserProperty(UserDisablePopupConf disablePopup)
    {
        this.disablePopup = disablePopup;
    }
    
    @Extension
    public static class DescriptorImpl extends UserPropertyDescriptor
    {
        @Override
        public UserProperty newInstance(User user)
        {
            return new DisablePopupUserProperty(UserDisablePopupConf.NOCONF);
        }
        
        @Override
        public String getDisplayName()
        {
            return Messages.DisablePopupUserProperty_DisplayName();
        }
    }
    
    public static enum UserDisablePopupConf {
        NOCONF(Messages._UserDisablePopupConf_NOCONF_DisplayName()),
        TRUE(Messages._UserDisablePopupConf_TRUE_DisplayName()),
        FALSE(Messages._UserDisablePopupConf_FALSE_DisplayName());
        
        private Localizable name;
        
        private UserDisablePopupConf(Localizable name)
        {
            this.name = name;
        }
        
        public String getDisplayName()
        {
            return name.toString();
        }
    }
}
