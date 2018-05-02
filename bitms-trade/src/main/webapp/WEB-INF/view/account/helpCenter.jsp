<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<html>
<body>
<div class="container">
	<%--头部--%>
	<%@ include file="/global/topNavBar.jsp" %>
	<%--代码开始--%>
	<div class="row">
		<%@ include file="/global/topPublicNav.jsp" %>
		<div class="col-sm-12 column">
			<%--开始代码位置--%>
			<div class="panel">
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 bitms-c2">
						<ul class="nav nav-tabs bitms-tabs" style="background-color:transparent;margin-right:0px;margin-top:-15px;margin-bottom: 20px">
							<%--<li>
								<a href="#helpCenter" data-toggle="tab">&lt;%&ndash;帮助&ndash;%&gt;<fmt:message key="topNav.help" /></a>
							</li>--%>
							<li class="active">
								<a href="#userAgreement" data-toggle="tab"><%--用户协议--%><fmt:message key="topNav.userAgreement" /></a>
							</li>
							<li>
								<a href="#contactUs" data-toggle="tab"><%--联系我们--%><fmt:message key="topNav.contactUs" /></a>
							</li>
						</ul>
						<div class="tab-content">
							<%--<div class="tab-pane" id="helpCenter">
								<p><fmt:message key="introduction.txt1" />&lt;%&ndash;BITMS LIMITED（以下简称“本公司”）是一家根据英属维尔京群岛相关法律在英属维尔京群岛注册的公司，本公司运营网站：www.bitms.com及相关移动应用（以下简称“平台”或“本平台”），并基于该平台为用户提供数字货币之间的交易及相关服务。&ndash;%&gt;</p>
								<br>
								<h3><fmt:message key="introduction.title2" />&lt;%&ndash;保证金交易&ndash;%&gt;</h3>
								<p><fmt:message key="introduction.txt2" />&lt;%&ndash;BITMS 的用户从保证金融资系统进行融资融币，通过引入杠杆可以增大收益，最高可进行三倍杠杆的交易，当然也同时承担较大损失的风险。平台首创使用自动借贷还款功能：自动借贷系统，免除了借贷的操作，以免错过了最佳交易时机，自动还款系统，免除了用户忘记还款产生的利息。平台首创采用了有效负债计息模式，即真正被使用交易的负债计息，委托冻结中的负债不计息，更加有利于用户使用杠杆。借贷的利息采用定时计息方式，于每天香港时间0时，对全平台用户有效负债进行一次计息（息率视市场情况会进行调整），委托中的订单产生的债务以及当日借出并当然归还的债务不计息。&ndash;%&gt;</p>
								<p><fmt:message key="introduction.txt3" />&lt;%&ndash;BITMS的采用全仓保证金制度，且有负债时，资产不能往外提而增加风险。当用户的资产不足负债资产的110%时触发爆仓，委托价格根据账户内资产100%偿还负载资产计算所得。&ndash;%&gt;</p>
								<br>
								<h3><fmt:message key="introduction.title3" />&lt;%&ndash;安全策略&ndash;%&gt;</h3>
								<p><fmt:message key="introduction.txt4" />&lt;%&ndash;BITMS为了保证账户资产安全，用户必须绑定手机以及谷歌验证器，并且设置资金密码后才能进行充值提现，交易。用户可以去安全中心进行相关设置。&ndash;%&gt;</p>
								<br>
								<h3><fmt:message key="introduction.title4" />&lt;%&ndash;交易市场&ndash;%&gt;</h3>
								<p><fmt:message key="introduction.txt5" />&lt;%&ndash;BITMS用户在充值后，可以进入现货交易市场进行相关交易BITMS平台采用T+0交易模式，24小时随时成交。下单采取限价单制度，即指定价格或者更优价格买入的订单，此类订单可在完全成交前取消（撤销），确保可以以满意的价格成交。交易成功后平台将收取相应的交易手续费，交易手续费将从您的成交总额中扣除，若成交后获得比特币资产，则支付比特币交易手续费。（手续费息率视市场情况会进行调整）&ndash;%&gt;</p>
							</div>--%>
							<div class="tab-pane active" id="userAgreement">
								<p>BITMS LIMITED (hereinafter referred to as BITMS) is a company registered in the British Virgin Islands under the applicable laws and regulations. With its platform, including the operating website:  www.bitms.com and the related mobile applications (hereinafter referred to as "the platform"), BITMS provides its users with services concerning digital currency trading. </p>
								<h4 class="text-danger"><span class="glyphicon glyphicon-exclamation-sign mr10"></span>IMPORTANT NOTES:</h4>
								<p class="text-danger">Digital asset trading can be extremely risky. It is open throughout the day without up and down limits, and the prices are subject to drastic fluctuation under the influence of market makers and worldwide government policies. Furthermore, due to the drafting or modification of laws, regulations and regulatory documents in various countries, digital asset trading may be suspended or banned at any time. You should carefully consider and be totally clear about the above risks and bear the resulting loss. BITMS does not assume any responsibility in these cases.</p>
								<h4>I. Range of application</h4>
								<p>This service is only available to users of age 18 years or older, hence you have to declare and ensure that you are at least 18 years old and have not been deprived of the corresponding rights. In addition, the platform temporarily does not provide services to the following countries and regions: Cuba, Iran, North Korea, Iraq, Belarus, Sudan, Libya, Mainland China and the United States of America. If you fail to meet these requirements, please do not use our service.</p>
								<h4>II. Service Content and Rules</h4>
								<p>The platform provides only online trading platform services for you to conduct digital currency transaction activities (including but not limited to digital asset trading), and the platform itself does not participate as a buyer or seller in the sale of digital assets.</p>
								<p>&nbsp;&nbsp;1.) You have the right to view the real-time quotes and trading information of the various digital asset products on the platform, and to submit digital asset trading instructions and complete the digital asset trading through the platform.</p>
								<p>&nbsp;&nbsp;2.) You should abide by the laws and regulations, and should properly use and keep your account number, login password, fund password, and the phone number bound at registering, as well as the verification code received by your mobile phone. For your use of the platform’s account, login password, fund password, verification code for any operation and the corresponding consequences, you have to take full responsibility. When you find that the account, login password, fund password, or the verification code of the platform might be used by unauthorized third parties, or if you find any other account security issues, you should effectively inform the platform and request the platform to suspend the services for the account in question. The platform reserves the right to take action on your request within a reasonable time, but for any consequences (including but not limited to any loss on your part) occurred before action taken, the platform assumes no responsibility whatsoever. You may not share with others the platform’s account by granting, lending, leasing, transferring or any other means without the consent of the platform.</p>
								<p>&nbsp;&nbsp;3.) While viewing the trading information on the platform, you should carefully read ALL the content, including but not limited to price, quota, fee, sale orientation (buy or sell) etc. If you fully accept all the content of the trading information, you can first confirm the information’s correction and then submit the trading commission.</p>
								<p>&nbsp;&nbsp;4.) If you use any methods, such as but not limited to technological means, system vulnerabilities etc., to make the commissioned trading conclude at a price deviated from the normal market quotation, or to make the account of assets unreasonably increase or decrease, BITMS has the right to freeze the relevant accounts permanently.</p>
								<h4>III. Service Change, Interruption or Termination</h4>
								<p>This Agreement shall remain in effect unless the platform terminates it or you apply to terminate it and obtain consent of the platform. If you violate this Agreement or the relevant rules, or at the request of relevant laws, regulations, government departments, the platform reserves the right to terminate this Agreement, close your account or limit your use of the platform.</p>
								<p>&nbsp;&nbsp;1.) Given the specific characteristics of network services, you agree that the platform has the right to change, interrupt or terminate some or all of the network services at any time, and in the event of such changes, interruptions or termination, the platform will not be subject to any further notice to the users and will not assume any responsibility for any user or any third party.</p>
								<p>&nbsp;&nbsp;2.) You understand that the platform needs to regularly or irregularly carry on overhauls or maintenance to the network service providing platforms (such as Internet websites, mobile networks, etc.) or the related equipment. If, due to such circumstance, the network service is interrupted within a reasonable time, the platform will need not to assume any responsibility but should notify in advance to the best of its ability.</p>
								<p>&nbsp;&nbsp;3.) Based on the unilateral independent judgment, and in the circumstances that it is considered that the trading might be vulnerable to dangers etc., the platform has the right to suspend, interrupt or terminate the provision of all or part of the services under this Agreement to you without prior notice, and to remove or delete the registration information without any liability to you or any third party. The foregoing circumstances include but are not limited to:</p>
								<p>&nbsp;&nbsp;&nbsp;&nbsp;(1) The platform does not consider the personal information provided by you to be authentic, valid or complete;</p>
								<p>&nbsp;&nbsp;&nbsp;&nbsp;(2) When the platform finds any trading abnormal or in doubt or in danger of breaking the law;</p>
								<p>&nbsp;&nbsp;&nbsp;&nbsp;(3) The platform considers that your account is involved in money laundering, pyramid selling, being falsely used or in other situations that the platform deems as risky;</p>
								<p>&nbsp;&nbsp;&nbsp;&nbsp;(4) Any other circumstances that based on trading security and other reasons, according to the platform’s unilateral judgment that all or part of the membership services under this Agreement provided to you need to be suspended, interrupted or terminated, and the registration information need to be removed or deleted.</p>
								<h4>IV. Service Fee</h4>
								<p>&nbsp;&nbsp;1.) When users use the service, according to the corresponding rules, the platform shall be entitled to charge the user service fees and interest generated by the loan. The platform has the right to formulate and adjust the service fees and the loan interest. Specific service fees and loan interest shall be notified by the billing method listed when the user start using the service or by other written agreement between users and the platform.</p>
								<p>&nbsp;&nbsp;2.) Unless otherwise stated or agreed, the user agrees that the platform shall be entitled to deduct directly the above service fees and loan interest from the assets of the user's account.</p>
								<h4>V. Limitation of Liability and Exemption</h4>
								<p>We assume no responsibility for any service inability or service delay caused by network equipment maintenance, the failure of information network, computer, telecommunication or other systems, power failure, weather, accidents, strikes, labor disputes, insurrections, uprisings, riots, shortage of productivity or means of production, floods, storms, explosions, wars, causes of banks or other partners, collapse of digital asset markets, government actions, orders of judicial or administrative authorities, and other causes that are not within our control or under our control, or causes from third parties. And for your resulting loss, we are not responsible either. We cannot guarantee that all the information, programs, texts, etc. contained in the platform are completely safe and free from the interference and destruction of any malicious programs such as viruses and Trojans. Therefore, it’s your own decisions to log in and use any service of the platform or download and use any program, information, data, etc., and you shall bear the risks and possible losses.</p>
							</div>
							<div class="tab-pane" id="contactUs">
								<p>Email：support@bitms.com</p>
								<p>Twitter：bitmscom</p>
								<p>Telegram：<a class="text-success" href="https://t.me/bitmscom" target="_blank">https://t.me/bitmscom</a></p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>