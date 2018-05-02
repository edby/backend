<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="row my0">
	<div class="col-xs-12 mt50">
		<div class="text-center"><a href="/" title="BitMS"><img src="${imagesPath}/bitms/bitms.svg" alt="BITMS"></a></div>
		<div class="p10 bitms-con1 text-center mt10">
			<div class="bitms-con1 py20 clearfix">
				<div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
					<form:form data-widget="validator" id="reg-step1">
						<div class="h2 text-center mb20 cf">SIGN UP</div>
						<div class="form-group ui-form-item">
							<input class="form-control input-lg" type="email" onkeydown="KeyDown();"  id="email" name="email" required
								   data-display="Email" placeholder="Email"/>
						</div>
						<div class="form-group">
							<div class="ln">
								<div id="captcha"></div>
								<input type='hidden' id='csessionid' name='csessionid'/>
								<input type='hidden' id='sig' name='sig'/>
								<input type='hidden' id='token' name='token'/>
								<input type='hidden' id='scene' name='scene'/>
							</div>
						</div>
						<div class="form-group">
							<button type="button" class="btn btn-primary btn-lg bitms-width" disabled id="regSubmit1" name="regSubmit1">Submit</button>
						</div>
						<div class="form-group mt-5 fs14 mb10 clearfix">
							<label for="agreeChk" class="pull-left" style="font-weight: 500;">
								<input type="checkbox" name="agreeChk" id="agreeChk"/>
								<span>I agree to the</span>
								<span class="text-primary" data-toggle="modal" data-target=".user-agreement">"Terms of Use"</span>
								<div class="modal fade user-agreement" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
									<div class="modal-dialog" role="document" style="height: 80%;overflow-y: scroll;">
										<div class="modal-content" style="border: solid 1px #3d5361;">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
												<h4 class="modal-title text-success text-left">Terms of Use</h4>
											</div>
											<div class="modal-body text-left fs12">
												<p>BITMS LIMITED (hereinafter referred to as BITMS) is a company registered in the British Virgin Islands under the applicable laws and regulations. With its platform, including the operating website: <a  class="text-success">www.bitms.com</a>  and the related mobile applications (hereinafter referred to as "the platform"), BITMS provides its users with services concerning digital currency trading. </p>
												<h3>IMPORTANT NOTES:</h3>
												<p>Digital asset trading can be extremely risky. It is open throughout the day without up and down limits, and the prices are subject to drastic fluctuation under the influence of market makers and worldwide government policies. Furthermore, due to the drafting or modification of laws, regulations and regulatory documents in various countries, digital asset trading may be suspended or banned at any time. You should carefully consider and be totally clear about the above risks and bear the resulting loss. BITMS does not assume any responsibility in these cases.</p>
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
										</div>
									</div>
								</div>
							</label>
							<span class="pull-right" style="line-height: 25px;">
								<span class="hidden-xs">Already have an account?</span>
								<a href="${ctx}/login" title="login" class="text-primary login-link">LOG IN</a>
							</span>
						</div>
						<div class="alert alert-danger text-left" role="alert" style="background-image: none;"><span class="glyphicon glyphicon-warning-sign text-danger pr10"></span>BITMS does not provide services for users in these countries or regions: Cuba, Iran, North Korea, Iraq, Belarus, Sudan, Libya, Chinese mainland and USA.</div>
					</form:form>
				</div>
				<div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
					<form id="reg-step2" class="hidden text-left">
						<div class="alert alert-success clearfix" style="background-image: none;" role="alert">
							<span class="pull-left">Please check your email to activate your account.</span>
							<a href="${forgetPass}/register" title="resend" class="text-danger pull-right">Resend</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<footer class="navbar-fixed-bottom bitms-con">
	<div class="fs12 text-center bitms-footer">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
<script src="${ctx}/scripts/src/account/register.js"></script>
</body>
</html>
