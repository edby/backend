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
							<li onclick="jumpUrl('${ctx}/account/helpCenter')">
								<a href="#" data-toggle="tab"><%--帮助--%><fmt:message key="topNav.help" /></a>
							</li>
							<li class="active">
								<a href="#" data-toggle="tab"><%--用户协议--%><fmt:message key="topNav.userAgreement" /></a>
							</li>
						</ul>
						<p>BITMS LIMITED（hereinafter referred to as “the company”）is an enterprise registered the British Virgin Islands according to related laws there. The company now runs a website <a class="text-success">www.bitms.com</a> as well as a related mobile application (hereinafter referred as “the platform”), at the same time, providing digital currency transaction services and related service on the basis of the platform.</p>
						<h3>Clarification</h3>
						<p>Digital assets trading is of high risks. The trade is around the clock and have no limit in price rising and falling. Huge price volatility is often seen in this market due to actions of influential investors and the changes of government policies. Additionally, digital assets trading can be suspended or prohibited at any time because of the enactment and amendment of certain laws, regulations and regulatory documents.</p>
						<h4>1 User identity</h4>
						<p>Only users at the age of 18 or above are intended for services on the platform. Please make sure that you are within the demanded age range,and not deprived of related rights. Additionally, BITMS does not provide services for users in theses countries or regions: Cuba, Iran, North Korea, Iraq, Belarus, Sudan, Libya, mainland China and USA. Thus, if unable to meet all these requirements, you are not allowed to enjoy these services.</p>
						<h4>2 Services and rules</h4>
						<p>The platform only provides users with online trading services regarding activities including but not limiting in digital assets trading through the platform. BITMS will not participate in digital assets trading.</p>
						<p>&nbsp;&nbsp;1) Users have the right to check trading information, submit digital assets trading orders and implement digital assets trading on the website.</p>
						<p>&nbsp;&nbsp;2) Users should abide by laws and conventions, safekeep and use his or her accounts, login passwords, payment passwords, cell phone numbers which are bound to the accounts as well as the verification codes through cell phones appropriately. Users should take full responsibilities for his or her actions to the web accounts, login passwords, payment passwords and verification codes through cell phones. If you find any security issues, for example, above items being used by an unauthorized third party, please contact us timely and effectively to suspend services under your accounts on the website. We will take measures to deal with your requests within proper range of time, but will not be responsible for any consequences—including but not limiting to your losses—caused before any measures. Users can not present, lend, rent or transfer the possession of his or her accounts to others without BITMS’s permission.</p>
						<p>&nbsp;&nbsp;3) Users should go through all trading information carefully, which includes but not limits to prices, amounts, service fees, trading directions (buy in or sell out), accept all of the trading rules and make sure trading information is correct before submitting orders.</p>
						<p>&nbsp;&nbsp;4) Users can not draw on technical approaches such as system vulnerability attacks to make the trading prices deviate from the normal market to increase or decrease account funds inappropriately. Once found, BITMS has the right to freeze his or her accounts permanently.</p>
						<h4>3 Modification/Suspension/Termination of Service</h4>
						<p>This contract should remain in force unless the platform terminates it, or users’ terminating application is approved by the platform. The platform has the right to terminate this contract, close users’ accounts or limit users’ operation on the website if users violate the agreement or related rules, or under the request of relevant laws, regulations or government sectors.</p>
						<p>&nbsp;&nbsp;1) Users should agree on BITMS’s right to change, suspend or terminate partial or all online services at anytime due to the particularity of online services. BITMS does not need to inform users separately, and it bears no obligations for any users or third parties.</p>
						<p>&nbsp;&nbsp;2) Users should understand that the platform and related utilities (including internet websites or mobile networks etc.) need regular or irregular checks for maintenance, thus, our platform will not be responsible for network interruption during this process, but BITMS will try its best to release notices in advance.</p>
						<p>&nbsp;&nbsp;3) BITMS has the right to, on the basis of its own judgement, suspend or terminate users’ partial or full services basing on this contract, transfer or remove users’ registered information before sending notices to users if any potential security issues happen. BITMS bears no responsibilities for users or the third parties under below circumstances.</p>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;a) The platform makes a conclusion that the user’s personal information is not authentic, valid or complete.</p>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;b) The platform finds any situations that contain abnormal, suspicious or illegal trading.</p>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;c) The User’s accounts are considered of risks for involving in money laundering,, pyramid selling, being stolen by other people, etc.</p>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;d) The platform will, on the basis of its independent judgement, suspend or terminate partial or all services stipulated in this contract, transfer or remove users’ registered information or take other measures before any notices for the sake of transaction security.</p>
						<h4>4 Service fees</h4>
						<p>&nbsp;&nbsp;1) BITMS has the right to charge users for service fees or debt interests when providing users related services. Besides, the platform can adjust service fees and borrowing interests accordingly. Users should refer to charging notices posted on the website and other paper agreements concluded with BITMS for detailed information and rules.</p>
						<p>&nbsp;&nbsp;2) The platform should be allowed to deduct service fees and borrowing interests from users’ accounts directly, unless specific explanation or agreements are made.</p>
						<h4>5 Limitation and Exemption of Liability</h4>
						<p>BITMS bears no responsibilities for losses caused by the third parties or by things beyond BITMS’s control like network equipment maintaining, network connection failure, system failure such as computer and communication issues, power failure, bad weather, accidents, strikes, labor disputes, riots, social uprising or unrest, the lack of productivity, means of productivity deficiency, fire, flood, storms disasters, explosion, warfare, partner cooperation problems, the collapse of digital assets market, government movements, judicial or administrational orders, etc. BITMS can’t ensure the security of all information, programs or files on the website, which may be impacted by the interference and destruction of malwares. Thus, users need to take risks and losses during the process of login, enjoying services, downloading and using any programs, information or data on the website.</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>