package com.fahai.cc.service.mail;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class EmailVo
{
	
	// 发件服务器
	@Value("#{setting['systemEmail.host']}")
	private String host;
	//邮件发送者
	@Value("#{setting['systemEmail.sender']}")
    private String sender;
    //发件人邮箱密码
	@Value("#{setting['systemEmail.password']}")
    private String password;
	//邮件接收者
    private String [] receivers;

    // 邮件抄送人
    private String [] cc;

    //邮件抄送人
    private String [] bcc;

    //Email发送的内容
    private String emailContent;

    //邮件主题
    private String subject;

    //邮件附件
    private File [] attachFile;

    public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getEmailContent()
    {
        return emailContent;
    }

    public void setEmailContent(String emailContent)
    {
        this.emailContent = emailContent;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public File[] getAttachFile()
    {
        return attachFile;
    }

    public void setAttachFile(File[] attachFile)
    {
        this.attachFile = attachFile;
    }

    public String[] getReceivers()
    {
        return receivers;
    }

    public void setReceivers(String[] receivers)
    {
        this.receivers = receivers[0].split(",");
    }

    public String[] getCc()
    {
        return cc;
    }

    public void setCc(String[] cc)
    {
        this.cc = cc;
    }

    public String[] getBcc()
    {
        return bcc;
    }

    public void setBcc(String[] bcc)
    {
        this.bcc = bcc;
    }
}
