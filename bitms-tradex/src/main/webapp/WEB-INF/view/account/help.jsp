<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-left loginCon" style="max-width:1080px;">
            <h2 class="loginTit text-center">Help</h2>
            <hr>
            <h4 class="text-primary">Does BIEX support all Token trading?</h4>
            <p>BIEX is committed to providing an autonomous and democratic exchange platform for all digital assets. In the v1.0 version, we already support all ERC20 Tokens. In the future, we will support any public-chain Tokens with a significant market share.</p>
            <br>
            <h4 class="text-primary">How to activate a trading pair?</h4>
            <p>Find a trading pair by entering its Token symbol，name or contract address. Click "Unactivated" and pay 1,000,000 BIEX to activate it. An activated trading pair can be used and traded by all users. The default valid service period for a trading pair is 1 year and needs to be reactivated after this period expires. The BIEX Candy can be obtained through the BIEX/ETH trading pair. <a href="/exchange?contractAddr=0x806336c912762274bfc4d0f78b1be2c0119e86f0">Go to exchange.</a></p>
            <br>
            <h4 class="text-primary">Why can't I find a Token?</h4>
            <p>Please check if the Token your search meets the following conditions:</p>
            <p>Total Supply: between 100,000 and 100,000,000,000</p>
            <p>Symbol: 2-6 English letters</p>
            <p>Name: Within 15 English letters</p>
            <br>
            <h4 class="text-primary">How to get a 5% discount when activating trading pairs?</h4>
            <p>Please enter a correct invitation code in the activation page.</p>
            <br>
            <h4 class="text-primary">How to destroy the BIEX Candy?</h4>
            <p>The total supply of BIEX is 5 billion, the contract address is: 0x806336c912762274bfc4d0f78b1be2c0119e86f0. The BIEX used for activating a trading pair will be transferred to this address [0x000000000000000000000000000000000000dead] for destruction.</p>
            <br>
            <h4 class="text-primary">Is there an incentive for inviting someone to activate a Token?</h4>
            <p>Click on “My invitation code”. When someone else enters your invitation code into the activation page, you will get 200,000 BIEX when the Token trading pair is activated.</p>
            <br>
            <h4 class="text-primary">How to ensure safety of the user assets?</h4>
            <p>At present, we adopt a fully off-line signature mechanism. No private key will be stored on the server. All withdrawals are paid using 100% off-line signatures. That means, each withdrawal by a user must come from a device completely disconnected from the network.</p>
            <br>
            <h4 class="text-primary">How is the trading fee charged?</h4>
            <p>The current policy is：charge 0.1% when buying, no charge when selling.</p>
            <br>
            <h4 class="text-primary">Do I have to bind Google Authenticator?</h4>
            <p>Google Authenticator is an important safety protection tool. We strongly recommend that you turn it on. In the withdrawal process, the Google Authenticator code must be verified.</p>
            <br>
            <h4 class="text-primary">How long does it take to complete a withdrawal?</h4>
            <p>Since we use a fully off-line signature mechanism, each withdrawal requires a physically disconnected signature, so it takes some time. The withdrawal process will be completed within 24 hours.</p>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
</body>
</html>