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

import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.model.PageDecorator;
import hudson.model.User;
import hudson.util.VersionNumber;

/**
 *
 */
@Extension
public class DisablePopupPageDecorator extends PageDecorator
{
    private boolean disablePopup = true;
    /**
     * @return the disablePopup
     */
    public boolean isDisablePopup()
    {
        return disablePopup;
    }
    
    /**
     * @param disablePopup the disablePopup to set
     */
    protected void setDisablePopup(boolean disablePopup)
    {
        this.disablePopup = disablePopup;
    }
    
    /**
     * 
     */
    public DisablePopupPageDecorator()
    {
        load();
    }
    
    /**
     * @param req
     * @param json
     * @return
     * @throws hudson.model.Descriptor.FormException
     * @see hudson.model.Descriptor#configure(org.kohsuke.stapler.StaplerRequest, net.sf.json.JSONObject)
     */
    @Override
    public boolean configure(StaplerRequest req, JSONObject json)
            throws hudson.model.Descriptor.FormException
    {
        setDisablePopup(json.getBoolean("disablePopup"));
        save();
        return true;
    }
    
    /**
     * @return
     * @see hudson.model.PageDecorator#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return Messages.DisablePopupPageDecorator_DisplayName();
    }
    
    /**
     * @return the disablePopup
     */
    public boolean isDisablePopupForCurrentUser()
    {
        return isDisablePopupForUser(User.current());
    }
    
    /**
     * @param user
     * @return
     */
    public boolean isDisablePopupForUser(User user)
    {
        switch(DisablePopupUserProperty.getDisablePopupForUser(user))
        {
        case TRUE:
            return true;
        case FALSE:
            return false;
        case NOCONF:
        default:
            break;
        }
        return isDisablePopup();
    }
    
    // I'm not sure transient is needed.
    private static transient Boolean applicableJenkinsVersion = null;
    /**
     * Only work for Jenkins < 1.510.
     * 
     * @return
     */
    public boolean isApplicableJenkinsVersion()
    {
        if(applicableJenkinsVersion == null)
        {
            applicableJenkinsVersion = Jenkins.getVersion().isOlderThan(new VersionNumber("1.510"));
        }
        return applicableJenkinsVersion;
    }
}
