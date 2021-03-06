<#--
 #%L
 ActivityInfo Server
 %%
 Copyright (C) 2009 - 2013 UNICEF
 %%
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the 
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public 
 License along with this program.  If not, see
 <http://www.gnu.org/licenses/gpl-3.0.html>.
 #L%
-->
<#include "Scaffolding.ftl">
<@scaffolding title="${label.signUpTitle}">

    <@content>
    <div class="row-fluid">
        <div class="span12">
                            
            <#if genericError == true || formError == true>
                <div class="alert alert-error">
                    ${label.signUpGenericError}
                </div>
            </#if>
        
            <#if confirmationEmailSent == true>
                <div class="alert alert-success">               
                    ${label.signUpEmailSent}
                </div>
           
            <#else>
                <h3>${label.signUpTitle}</h3>
                
                <form action="" method="post" id="signUpForm">
                
                    <div class="control-group" id="nameGroup">
                        <label class="control-label" for="nameInput">${label.name}:</label>
                        <div class="controls">
                            <input type="text" name="name" id="nameInput" value="${name}">
                            <span class="help-inline hide" id="nameHelp">${label.pleaseEnterYourFullName}</span>
                        </div>
                    </div>
                    
                    <div class="control-group">
                        <label class="control-label" for="organizationInput">${label.organization}:</label>
                        <div class="controls">
                            <input type="text" name="organization" id="organizationInput" value="${organization}">
                        </div>
                    </div>
                    
                    <div class="control-group">
                        <label class="control-label" for="jobtitleInput">${label.jobtitle}:</label>
                        <div class="controls">
                            <input type="text" name="jobtitle" id="jobtitleInput" value="${jobtitle}">
                        </div>
                    </div>
                    
                    <div class="control-group" id="emailGroup">
                        <label class="control-label" for="emailInput">${label.emailAddress}:</label>
                        <div class="controls">
                            <input type="text" name="email" id="emailInput" value="${email}">
                            <span class="help-inline hide" id="emailHelp">${label.pleaseEnterAValidEmailAddress}</span>
                        </div>
                    </div>
                    <#if emailExistsError == true>
                        <div class="alert alert-error">
                            ${label.signUpEmailExistsError}
                        </div>
                    </#if>
                        
                    <div class="control-group">
                        <label class="control-label" for="localeInput">${label.preferredLanguage}:</label>
                        <div class="controls">
                              <select name="locale" id="localeInput">
                                  <option value="en">${label.english}</option>
                                  <option value="fr">${label.francais}</option>
                              </select>
                        </div>
                    </div>
                    
                    <div class="control-group">
                        <label class="control-label">&nbsp;</label>
                        <div class="controls">
                            <button type="submit" class="btn btn-primary btn-large">${label.signUpButton}</button>
                        </div>
                    </div>
                </form>
            </#if>
            
        </div>
    </div>
    </@content>
    
    <@scripts>
    <script type="text/javascript">
        var validateName = function() {
            var valid = !!( $('#nameInput').val() );
            $('#nameGroup').toggleClass('error', !valid);
            $('#nameHelp').toggleClass('hide', valid);
            return valid;
        };
        
        var validateEmail = function() {
            var email = $('#emailInput').val();
            var valid = !!email;
            if (valid) {
                var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                valid = regex.test(email);
            }
            $('#emailGroup').toggleClass('error', !valid);
            $('#emailHelp').toggleClass('hide', valid);
            return valid;
        };
    
        $("#nameInput").change(validateName);
        $("#emailInput").change(validateEmail);
        $("#signUpForm").submit(function() {
            var valid = validateName() && validateEmail();
            return !!valid;
        });
        
        $(document).ready(function () {
            $("#localeInput").val("${locale}");
            
            <#if emailExistsError == true>
                $("#emailInput").focus();
            <#else>  
                $("#nameInput").focus();
            </#if>
        });
    </script>
    </@scripts>
</@scaffolding>
