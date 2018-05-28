##Spring Boot和OAuth2

本指南将向您展示如何使用OAuth2和Spring Boot构建一个示例应用，通过"social login"完成各种各样的事情。它从一个简单的单一提供者单点登录开始，并且可以运行一个自我托管的OAuth2授权服务器，并具有一组身份验证提供程序(Facebook或Github)。这些示例都是使用后端使用Spring Boot和Spring OAuth的单页应用程序。他们也都在前端使用普通的jQuery，但是转换为不同的JavaScript框架或使用服务器端呈现所需的更改将很少。

由于其中一个示例是完整的OAuth2授权服务器，因此我们使用了支持从Spring Boot 2.0到旧的Spring Security OAuth2库进行桥接的[shim](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/htmlsingle/)JAR。更简单的示例也可以使用Spring Boot安全特性中的本机OAuth2支持来实现。配置非常相似。

有几个样本相互构建，增加了新功能：

- simple：一个非常基本的静态应用程序，只有一个主页，并通过Spring Boot无条件登录`@EnableOAuth2Sso`(如果您访问主页，您将自动重定向到Facebook)。
- click：添加用户必须单击才能登录的显式链接。
- logout：为经过认证的用户添加注销链接。
- manual：`@EnableOAuth2Sso`通过取消选中并手动配置所有作品来显示作品的工作方式。
- github：在Github中添加了第二个登录提供程序，因此用户可以在主页上选择使用哪一个。
- auth-server：将应用程序变成一个完整的OAuth2授权服务器，能够发布自己的令牌，但仍然使用外部OAuth2提供程序进行身份验证。
- custom-error：为未经身份验证的用户添加错误消息，并基于Github API进行自定义身份验证。

它们中的每一个都可以导入到IDE中，并且有一个主要类SocialApplication可以在那里运行以启动应用程序。他们都在http://localhost：8080上提供了一个主页(并且如果您想登录并查看内容，都需要您至少有一个Facebook帐户)。您还可以运行使用命令行中的所有应用程序mvn spring-boot:run或通过建立jar文件，并运行它mvn package和java -jar target/*.jar。如果您在顶层使用包装，则无需安装Maven ，例如

	$ cd si    mple
    $ ../mvnw package
    $ java -jar target/*.jar

这些应用程序都可以localhost:8080使用，因为他们使用在Facebook和Github上注册的OAuth2客户端来访问该地址。要在不同的主机或端口上运行它们，您需要注册自己的应用程序并将凭据放入配置文件中。如果您使用默认值，则不会在本地主机之外泄漏您的Facebook或Github凭据，但要小心您在互联网上公开的内容，并且不要将您自己的应用程序注册置于公共源代码管理中。


##单点登录与Facebook
在本节中，我们创建一个使用Facebook进行身份验证的最小应用程序。如果我们利用Spring Boot中的自动配置功能，这将非常简单。

##创建一个新项目
首先，我们需要创建一个Spring Boot应用程序，这可以通过多种方式完成。最简单的是去http://start.spring.io并生成一个空的项目(选择"Web"依赖项作为起点)。等同于在命令行上执行此操作：

	$ mkdir ui && cd ui
	$ curl https://start.spring.io/starter.tgz -d style=web -d name=simple | tar -xzvf -
然后，您可以将该项目导入到您最喜欢的IDE中(默认情况下这是一个普通的Maven Java项目)，或者只是在命令行上使用这些文件和"mvn"。

##添加主页
在你的新项目中创建一个index.html"src/main/resources/static"文件夹。你应该添加一些样式表和Java脚本链接，如下所示：

	<!doctype html>
	<html lang="en">
	<head>
	    <meta charset="utf-8"/>
	    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	    <title>Demo</title>
	    <meta name="description" content=""/>
	    <meta name="viewport" content="width=device-width"/>
	    <base href="/"/>
	    <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/css/bootstrap.min.css"/>
	    <script type="text/javascript" src="/webjars/jquery/jquery.min.js"></script>
	    <script type="text/javascript" src="/webjars/bootstrap/js/bootstrap.min.js"></script>
	</head>
	<body>
		<h1>Demo</h1>
		<div class="container"></div>
	</body>
	</html>

这些都不是证明OAuth2登录功能所必需的，但我们希望最终有一个漂亮的UI，所以我们不妨从主页中的一些基本内容开始。

如果您启动应用程序并加载主页，您会注意到样式表尚未加载。所以我们也需要添加这些，我们可以通过添加一些依赖来实现：

	pom.xml
	<dependency>
		<groupId>org.webjars</groupId>
		<artifactId>jquery</artifactId>
		<version>2.1.1</version>
	</dependency>
	<dependency>
		<groupId>org.webjars</groupId>
		<artifactId>bootstrap</artifactId>
		<version>3.2.0</version>
	</dependency>
	<dependency>
		<groupId>org.webjars</groupId>
		<artifactId>webjars-locator-core</artifactId>
	</dependency>


我们添加了Twitter bootstrap和jQuery(这是我们现在需要的)。另一个依赖是webjars"locator"，它由webjars站点作为一个库提供，并且可以被Spring用来在webjars中定位静态资产，而不需要知道确切的版本(因此它是无版本的/webjars/**链接在index.html)。只要不关闭MVC自动配置，webjar定位器就会在Spring Boot应用中默认激活。

随着这些变化，我们应该有一个漂亮的主页为我们的应用程序。

##确保应用程序安全
为了使应用程序安全，我们只需要添加Spring Security作为依赖项。如果我们这样做，默认情况下是使用HTTP Basic来保护它，所以既然我们想做一个"社交"登录(委托给Facebook)，我们也添加了Spring Security OAuth2依赖项：

	pom.xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-security</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.security.oauth.boot</groupId>
		<artifactId>spring-security-oauth2-autoconfigure</artifactId>
		<version>2.0.0.RELEASE</version>
	</dependency>

为了建立到Facebook的链接，我们需要`@EnableOAuth2Sso`在我们的主类上添加注释：

	
	SocialApplication.java
	@SpringBootApplication
	@EnableOAuth2Sso
	public class SocialApplication {
	
	  ...
	
	}

配置 application.properties 建议使用可读性高的YAML

	application.yml
	security:
	  oauth2:
	    client:
	      clientId: 233668646673605
	      clientSecret: 33b17e044ee6a4fa383f46ec6e28ea1d
	      accessTokenUri: https://graph.facebook.com/oauth/access_token
	      userAuthorizationUri: https://www.facebook.com/dialog/oauth
	      tokenName: oauth_token
	      authenticationScheme: query
	      clientAuthenticationScheme: form
	    resource:
	      userInfoUri: https://graph.facebook.com/me
	...

该配置是指在其开发者网站上向Facebook注册的客户端应用程序，其中您必须为应用程序提供已注册的重定向(主页)。这个注册到"localhost：8080"，所以它只能在该地址上运行的应用程序中运行。

通过该更改，您可以再次运行该应用程序并访问http://localhost:8080的主页。而不是主页，你应该重定向到登录Facebook。如果你这样做，并接受你要求的任何授权，你将被重定向回到本地应用程序，主页将可见。如果您保持登录Facebook，即使您在全新的浏览器中打开它，但没有Cookie并且没有缓存数据，您也不必使用此本地应用重新进行身份验证。(这就是Single Sign On的意思。)

> 如果您正在使用示例应用程序完成本节，请务必清除Cookie和HTTP Basic凭据的浏览器缓存。在Chrome中，为单个服务器执行此操作的最佳方式是打开新的隐身窗口。

授予对此示例的访问权是安全的，因为只有在本地运行的应用程序可以使用令牌，并且它要求的范围有限。当你登录这样的应用程序时，要注意你正在批准的是什么：他们可能会要求你做比自己感觉舒服的事情更多的许可(例如，他们可能会要求允许更改你的个人数据，而这些数据可能不是你感兴趣的)。

## 刚刚发生了什么？ ##
您刚刚使用OAuth2术语编写的应用程序是客户端应用程序，它使用授权代码授权从Facebook(授权服务器)获取访问令牌。然后，使用访问令牌向Facebook询问一些个人信息(仅限于您允许的内容)，包括您的登录ID和您的姓名。在这个阶段，facebook充当资源服务器，解码您发送的令牌，并检查它是否允许应用访问用户的详细信息。如果该过程成功，则应用程序将用户详细信息插入到Spring Security上下文中，以便进行身份验证。

如果您查看浏览器工具(Chrome上的F12)，并按照所有跳跃的网络流量进行操作，您将会看到Facebook重定向，最后您会用新Set-Cookie标题重新登录主页。此Cookie(JSESSIONID默认情况下)是Spring(或任何基于servlet的)应用程序的认证详细信息的标记。

所以我们有一个安全的应用程序，就是说用户必须用外部提供商(Facebook)来验证任何内容。我们不希望将其用于网上银行网站，但为了基本识别目的，并将网站内不同用户之间的内容隔离开来，这是一个很好的起点，这就解释了为什么这种认证现在非常流行。在下一节中，我们将为应用程序添加一些基本功能，并且让用户看到最初重定向到Facebook时发生了什么变得更加明显。

## 添加欢迎页面 ##
在本节中，我们通过添加显式链接来登录Facebook来修改我们刚刚构建的简单应用程序。新链接不会立即被重定向，而会在主页上显示，用户可以选择登录或保持未经身份验证。只有当用户点击链接时，才会显示安全内容。

## 主页中的条件内容 ##
为了渲染一些内容，以用户是否被认证为条件，我们可以使用服务器端渲染(例如Freemarker或Tymeleaf)，或者我们可以使用一些JavaScript请求浏览器。要做到这一点，我们要使用Angular JS。

要开始使用动态内容，我们需要在其部分标记HTML中显示它：

	index.html
	<div class="container unauthenticated">
	    With Facebook: <a href="/login">click here</a>
	</div>
	<div class="container authenticated" style="display:none">
	    Logged in as: <span id="user"></span>
	</div>

此HTML使我们了需要操纵的一些客户端代码authenticated，unauthenticated和user元素。以下是这些功能的简单实现(将它们放在最后<body>)：

	index.html
	<script type="text/javascript">
	    $.get("/user", function(data) {
	        $("#user").html(data.userAuthentication.details.name);
	        $(".unauthenticated").hide()
	        $(".authenticated").show()
	    });
	</script>

## 服务器端更改 ##
为此，我们需要在服务器端进行一些更改。"home"控制器需要一个描述当前认证用户的"/user"端点。这很容易做到，例如在我们的主要课程中：

	SocialApplication
	@SpringBootApplication
	@EnableOAuth2Sso
	@RestController
	public class SocialApplication {
	
	  @RequestMapping("/user")
	  public Principal user(Principal principal) {
	    return principal;
	  }
	
	  public static void main(String[] args) {
	    SpringApplication.run(SocialApplication.class, args);
	  }
	
	}

请注意使用`@RestControllerand` `@RequestMapping`和`java.security.Principal`我们注入处理程序的方法。


> Principal在/user类似的端点中返回一个整体并不是一个好主意(它可能包含您不希望向浏览器客户端透露的信息)。我们只是为了快速开展工作。在本指南的后面，我们将转换端点来隐藏我们不需要浏览器的信息。

此应用现在可以像以前一样正常工作并进行身份验证，但不会让用户有机会点击我们刚刚提供的链接。为了使链接可见，我们还需要关闭主页上的安全性，方法是添加一个`WebSecurityConfigurer`：

	SocialApplication
	@SpringBootApplication
	@EnableOAuth2Sso
	@RestController
	public class SocialApplication extends WebSecurityConfigurerAdapter {
	
	  ...
	
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .antMatcher("/**")
	      .authorizeRequests()
	        .antMatchers("/", "/login**", "/webjars/**", "/error**")
	        .permitAll()
	      .anyRequest()
	        .authenticated();
	  }
	
	}

Spring Boot WebSecurityConfigurer对携带`@EnableOAuth2Sso`注释的类附加了特殊含义：它使用它来配置携带OAuth2身份验证处理器的安全筛选器。所以我们所需要做的就是显式`authorizeRequests()`地将主页和它所包含的静态资源(我们还包括对处理认证的登录端点的访问权限)进行明确显示。所有其他请求(例如到/user端点)都需要认证。

> /error** 是一种不受保护的路径，因为我们希望Spring Boot能够在应用程序出现问题时呈现错误，即使用户未经过身份验证。

随着这种变化，应用程序已经完成，如果你运行它并访问主页，你应该看到一个很好的HTML链接，用于"使用Facebook登录"。该链接不会将您直接转到Facebook，而是转到处理认证的本地路径(并将重定向发送到Facebook)。通过身份验证后，您将重定向回本地应用程序，并在其中显示您的姓名(假设您已在Facebook中设置了允许访问该数据的权限)。

# 添加注销按钮 #
在本节中，我们通过添加一个允许用户注销应用程序的按钮来修改我们构建的`click`应用程序。这似乎是一个简单的功能，但它需要执行一些小心，所以值得花一些时间讨论如何做到这一点。大部分变化都是因为我们正在将应用程序从只读资源转换为读写资源(注销需要状态更改)，所以在任何实际应用程序中都需要进行相同的更改不仅仅是静态的内容。

## 客户端变化 ##
在客户端上，我们只需提供一个注销按钮和一些JavaScript以回叫服务器来请求取消认证。首先，在UI的"已认证"部分，我们添加按钮：

	index.html
	<div class="container authenticated">
	  Logged in as: <span id="user"></span>
	  <div>
	    <button onClick="logout()" class="btn btn-primary">Logout</button>
	  </div>
	</div>

然后我们提供`logout()`它在JavaScript中引用的函数：

	index.html
	var logout = function() {
	    $.post("/logout", function() {
	        $("#user").html('');
	        $(".unauthenticated").show();
	        $(".authenticated").hide();
	    })
	    return true;
	}

该`logout()`函数执行POST到`/logout`并清除动态内容。现在我们可以切换到服务器端来实现该端点。

## 添加注销端点 ##
Spring Security已经构建了对`/logout`端点的支持，它将为我们做正确的事情(清除会话并使Cookie无效)。要配置端点，我们只需在现有的`configure()`方法中扩展现有的方法`WebSecurityConfigurer`：

	SocialApplication.java
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.antMatcher("/**")
	    ... // existing code here
	    .and().logout().logoutSuccessUrl("/").permitAll();
	}

`/logout` 端点需要我们POST，并保护用户免受跨站点请求伪造（CSRF，发音为“sea surf”），它需要在请求中包含令牌。令牌的值与当前会话相关联，这是提供保护的，因此我们需要一种方法将这些数据导入JavaScript应用程序。

许多JavaScript框架都支持CSRF(例如，在Angular中称它为XSRF)，但它通常以与Spring Security的现成行为稍有不同的方式实现。例如，在Angular中，前端希望服务器向它发送一个名为"XSRF-TOKEN"的cookie，如果它看到了，它会将该值作为名为"X-XSRF-TOKEN"的标题发回。我们可以使用我们简单的jQuery客户端实现相同的行为，然后服务器端的更改将与其他前端实现一起工作，而无需或仅有很少的更改。为了向Spring Security讲授这一点，我们需要添加一个创建cookie的过滤器，同时我们还需要告诉现有的CRSF过滤器关于标题名称。在`WebSecurityConfigurer`：

	SocialApplication.java
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.antMatcher("/**")
	    ... // existing code here
	    .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

## 在客户端添加CSRF令牌 ##
由于我们在本示例中没有使用更高级别的框架，因此我们需要明确添加CSRF令牌，我们可以从后端将cookie作为cookie提供。为了使代码更简单一些，我们添加了一个额外的库：

	pom.xml
	<dependency>
	    <groupId>org.webjars</groupId>
	    <artifactId>js-cookie</artifactId>
	    <version>2.1.0</version>
	</dependency>

以HTML格式导入：

	index.html
	<script type="text/javascript" src="/webjars/js-cookie/js.cookie.js"></script>

那么我们可以Cookies在xhr中使用便捷方法：

	index.html
	$.ajaxSetup({
	beforeSend : function(xhr, settings) {
	  if (settings.type == 'POST' || settings.type == 'PUT'
	      || settings.type == 'DELETE') {
	    if (!(/^http:.*/.test(settings.url) || /^https:.*/
	        .test(settings.url))) {
	      // Only send the token to relative URLs i.e. locally.
	      xhr.setRequestHeader("X-XSRF-TOKEN",
	          Cookies.get('XSRF-TOKEN'));
	    }
	  }
	}
	});

## 准备！ ##
随着这些变化，我们准备运行应用程序并尝试新的注销按钮。启动应用程序并在新的浏览器窗口中加载主页。点击"登录"链接将您带到Facebook(如果您已经登录，您可能不会注意到重定向)。点击"注销"按钮取消当前会话并将应用程序返回至未经身份验证的状态。如果您好奇，您应该能够在浏览器与本地服务器交换的请求中看到新的cookie和标头。

请记住，现在注销端点正在与浏览器客户端协同工作，那么所有其他HTTP请求(POST，PUT，DELETE等)也可以正常工作。所以这应该是一个具有更现实功能的应用程序的良好平台。

# 手动配置OAuth2客户端 #
在本节中，我们通过选择注释中的"魔术"来修改我们已经构建的注销应用程序`@EnableOAuth2Sso`，并手动配置其中的所有内容以使其变得清晰。

## 客户和认证 ##
背后有两个功能@EnableOAuth2Sso：OAuth2客户端和身份验证。客户端可以重复使用，因此您还可以使用它与您的授权服务器(本例中为Facebook)提供的OAuth2资源(本例中为Graph API)进行交互。身份验证部分将您的应用程序与Spring Security的其余部分对齐，所以一旦与Facebook共舞结束您的应用程序的行为与其他任何安全的Spring应用程序完全相同。

客户端部分由Spring Security OAuth2提供，并由不同的注释开启@EnableOAuth2Client。因此，此转换的第一步是删除`@EnableOAuth2Sso`并用较低级别的注释替换它：

	SocialApplication
	@SpringBootApplication
	@EnableOAuth2Client
	@RestController
	public class SocialApplication extends WebSecurityConfigurerAdapter {
	  ...
	}

一旦完成，我们为我们创建了一些有用的东西。首先，我们可以注入OAuth2ClientContext并使用它来构建我们添加到安全配置中的认证过滤器：

	SocialApplication
	@SpringBootApplication
	@EnableOAuth2Client
	@RestController
	public class SocialApplication extends WebSecurityConfigurerAdapter {
	
	  @Autowired
	  OAuth2ClientContext oauth2ClientContext;
	
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http.antMatcher("/**")
	      ...
	      .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	  }
	
	  ...
	
	}

这个过滤器是在新方法中创建的，我们使用OAuth2ClientContext：

	SocialApplication
	private Filter ssoFilter() {
	  OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
	  OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
	  facebookFilter.setRestTemplate(facebookTemplate);
	  UserInfoTokenServices tokenServices = new UserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
	  tokenServices.setRestTemplate(facebookTemplate);
	  facebookFilter.setTokenServices(tokenServices);
	  return facebookFilter;
	}

该过滤器还需要了解Facebook上的客户端注册：

	SocialApplication
	  @Bean
	  @ConfigurationProperties("facebook.client")
	  public AuthorizationCodeResourceDetails facebook() {
	    return new AuthorizationCodeResourceDetails();
	  }
并完成它需要知道用户信息终端位于Facebook的认证的认证：

	SocialApplication
	  @Bean
	  @ConfigurationProperties("facebook.resource")
	  public ResourceServerProperties facebookResource() {
	    return new ResourceServerProperties();
	  }

请注意，对于这两个"静态"数据对象(facebook()和facebookResource())我们都使用了@Bean装饰@ConfigurationProperties。这意味着我们可以将其转换application.yml为稍微新的格式，其中配置的前缀facebook不是security.oauth2：

	application.yml
	facebook:
	  client:
	    clientId: 233668646673605
	    clientSecret: 33b17e044ee6a4fa383f46ec6e28ea1d
	    accessTokenUri: https://graph.facebook.com/oauth/access_token
	    userAuthorizationUri: https://www.facebook.com/dialog/oauth
	    tokenName: oauth_token
	    authenticationScheme: query
	    clientAuthenticationScheme: form
	  resource:
	    userInfoUri: https://graph.facebook.com/me

最后，我们在Filter上面的声明中将登录的路径更改为专用于Facebook的路径，因此我们需要在HTML中进行相同的更改：

	index.html
	<h1>Login</h1>
	<div class="container unauthenticated">
		<div>
		With Facebook: <a href="/login/facebook">click here</a>
		</div>
	</div>

处理重定向
我们需要做的最后一项变化是明确支持从我们的应用程序重定向到Facebook。这是在Spring OAuth2中用servlet处理的，Filter因为我们使用了，所以过滤器在应用程序上下文中已经可用@EnableOAuth2Client。所有需要的是连接过滤器，以便在Spring Boot应用程序中按照正确的顺序调用过滤器。要做到这一点，我们需要一个FilterRegistrationBean：

	SocialApplication.java
	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
	    OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	}

我们自动装配已经可用的过滤器，并具有足够低的顺序，此番注册它之前的主要Spring Security的过滤器。通过这种方式，我们可以使用它来处理通过验证请求中的指示信号发送的重定向。

通过这些更改，应用程序可以很好地运行，并且在运行时等同于我们在上一部分中构建的注销示例。打破配置并明确表示，Spring Boot没有什么神奇的东西(它只是配置锅炉板)，它还准备了我们的应用程序，用于扩展自动提供的功能，添加我们自己的意见和业务需求。

用Github登录
在本节中，我们修改已经创建的应用程序，添加一个链接，以便用户可以选择使用Github进行身份验证，以及原始链接到Facebook。

添加Github链接
在客户端中，变更很简单，我们只需添加另一个链接：

	index.html
	<div class="container unauthenticated">
	  <div>
	    With Facebook: <a href="/login/facebook">click here</a>
	  </div>
	  <div>
	    With Github: <a href="/login/github">click here</a>
	  </div>
	</div>

原则上，一旦我们开始添加身份验证提供程序，我们可能需要更加小心从"/ user"端点返回的数据。事实证明，Github和Facebook在他们的用户信息中都有一个"名称"字段，所以在我们的简单终端实践中没有任何变化。

添加Github认证过滤器
服务器上的主要变化是添加一个额外的安全过滤器来处理来自我们新链接的"/ login / github"请求。我们已经在我们的ssoFilter()方法中为Facebook创建了自定义身份验证过滤器，因此我们需要做的就是用可以处理多个身份验证路径的组合替换它：

	SocialApplication.java
	private Filter ssoFilter() {
	
	  CompositeFilter filter = new CompositeFilter();
	  List<Filter> filters = new ArrayList<>();
	
	  OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
	  OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
	  facebookFilter.setRestTemplate(facebookTemplate);
	  UserInfoTokenServices tokenServices = new UserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
	  tokenServices.setRestTemplate(facebookTemplate);
	  facebookFilter.setTokenServices(tokenServices);
	  filters.add(facebookFilter);
	
	  OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
	  OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(github(), oauth2ClientContext);
	  githubFilter.setRestTemplate(githubTemplate);
	  tokenServices = new UserInfoTokenServices(githubResource().getUserInfoUri(), github().getClientId());
	  tokenServices.setRestTemplate(githubTemplate);
	  githubFilter.setTokenServices(tokenServices);
	  filters.add(githubFilter);
	
	  filter.setFilters(filters);
	  return filter;
	
	}

我们的旧代码ssoFilter()已被复制，一次用于Facebook，一次用于Github，两个过滤器合并为一个复合。

需要注意的是，facebook()和facebookResource()方法已经补充了类似的方法github()和githubResource()：

	SocialApplication.java
	@Bean
	@ConfigurationProperties("github.client")
	public AuthorizationCodeResourceDetails github() {
		return new AuthorizationCodeResourceDetails();
	}
	
	@Bean
	@ConfigurationProperties("github.resource")
	public ResourceServerProperties githubResource() {
		return new ResourceServerProperties();
	}

和相应的配置：

	application.yml
	github:
	  client:
	    clientId: bd1c0a783ccdd1c9b9e4
	    clientSecret: 1a9030fbca47a5b2c28e92f19050bb77824b5ad1
	    accessTokenUri: https://github.com/login/oauth/access_token
	    userAuthorizationUri: https://github.com/login/oauth/authorize
	    clientAuthenticationScheme: form
	  resource:
	    userInfoUri: https://api.github.com/user

此处的客户详细信息已在Github上注册，并且地址localhost:8080(与Facebook相同)。

该应用程序现在可以运行并为用户提供在使用Facebook或Github进行身份验证之间的选择。

如何添加本地用户数据库
许多应用程序需要在本地保存有关其用户的数据，即使身份验证委派给外部提供者也是如此。我们不在这里展示代码，但是通过两个步骤很容易。

为您的数据库选择一个后端，并为User适合您需求的自定义对象设置一些存储库(例如使用Spring Data)，并且可以从外部认证中完全或部分地填充该对象。

User通过检查/user端点中的存储库为每个登录的唯一用户配置一个对象。如果已经有用户具有当前的身份Principal，则可以更新，否则创建。

提示：在User对象中添加一个字段以链接到外部提供程序中的唯一标识符(不是用户名，而是外部提供程序中帐户唯一的唯一标识符)。

承载授权服务器
在本节中，我们修改了我们构建的github应用程序，该应用程序通过将应用程序变为完整的OAuth2授权服务器，仍然使用Facebook和Github进行身份验证，但能够创建自己的访问令牌。然后，这些令牌可用于保护后端资源，或与我们碰巧需要以其他方式保护的其他应用程序一起执行SSO。

整理认证配置
在开始使用授权服务器功能之前，我们将清理两个外部提供程序的配置代码。在该ssoFilter()方法中有一些代码是重复的，所以我们将其拖入共享方法中：

	SocialApplication.java
	private Filter ssoFilter() {
	  CompositeFilter filter = new CompositeFilter();
	  List<Filter> filters = new ArrayList<>();
	  filters.add(ssoFilter(facebook(), "/login/facebook"));
	  filters.add(ssoFilter(github(), "/login/github"));
	  filter.setFilters(filters);
	  return filter;
	}

新的便捷方法具有旧方法中的所有重复代码：

	SocialApplication.java
	private Filter ssoFilter(ClientResources client, String path) {
	  OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
	  OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
	  filter.setRestTemplate(template);
	  UserInfoTokenServices tokenServices = new UserInfoTokenServices(
	      client.getResource().getUserInfoUri(), client.getClient().getClientId());
	  tokenServices.setRestTemplate(template);
	  filter.setTokenServices(tokenServices);
	  return filter;
	}

并且它使用一个新的包装对象ClientResources来合并OAuth2ProtectedResourceDetails和在应用的最后一个版本ResourceServerProperties中声明为单独的对象@Beans：

	SocialApplication.java
	class ClientResources {
	
	  @NestedConfigurationProperty
	  private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();
	
	  @NestedConfigurationProperty
	  private ResourceServerProperties resource = new ResourceServerProperties();
	
	  public AuthorizationCodeResourceDetails getClient() {
	    return client;
	  }
	
	  public ResourceServerProperties getResource() {
	    return resource;
	  }
	}

包装器用于@NestedConfigurationProperty指示注释处理器也为元数据抓取该类型，因为它不代表单个值而是完整的嵌套类型。
有了这个包装器，我们可以像以前一样使用相同的YAML配置，但是每个提供者只需一个方法：

	SocialApplication.java
	@Bean
	@ConfigurationProperties("github")
	public ClientResources github() {
	  return new ClientResources();
	}
	
	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebook() {
	  return new ClientResources();
	}

启用授权服务器
如果我们想将我们的应用程序变成OAuth2授权服务器，那么至少要开始使用一些基本功能(一个客户端以及创建访问令牌的能力)就不会有大惊小怪了。授权服务器只不过是一堆端点，它们在Spring OAuth2中作为Spring MVC处理程序实现。我们已经有一个安全的应用程序，所以它只是添加@EnableAuthorizationServer注释的问题：

	SocialApplication.java
	@SpringBootApplication
	@RestController
	@EnableOAuth2Client
	@EnableAuthorizationServer
	public class SocialApplication extends WebSecurityConfigurerAdapter {
	
	   ...
	
	}

Spring Boot将安装所有必要的端点并为它们设置安全性，前提是我们提供了一些我们想要支持的OAuth2客户端的细节：

	application.yml
	security:
	  oauth2:
	    client:
	      client-id: acme
	      client-secret: acmesecret
	      scope: read,write
	      auto-approve-scopes: '.*'

此客户端的等效facebook.client*和github.client*我们需要的外部认证。通过外部提供商，我们必须注册并获取客户端ID和秘密才能在我们的应用中使用。在这种情况下，我们提供了相同的功能，所以我们需要(至少一个)客户端才能正常工作。

我们设置了auto-approve-scopes一个匹配所有作用域的正则表达式。这并不一定是我们将这个应用程序放在真实系统中的地方，但它可以让我们快速开展工作，而无需重新设置Spring OAuth2在用户想要访问令牌时弹出的白标签审批页面。要向令牌授予添加明确的批准步骤，我们需要提供替换白标签版本的界面(at /oauth/confirm_access)。
为了完成授权服务器，我们只需要为其UI提供安全配置。事实上，在这个简单的应用程序中没有太多的用户界面，但我们仍然需要保护/oauth/authorize端点，并确保具有"登录"按钮的主页可见。这就是为什么我们有这种方法：

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.antMatcher("/**")                                       (1)
	    .authorizeRequests()
	      .antMatchers("/", "/login**", "/webjars/**").permitAll() (2)
	      .anyRequest().authenticated()                            (3)
	    .and().exceptionHandling()
	      .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")) (4)
	    ...
	}

1	所有请求都被默认保护
2	主页和登录端点被明确排除
3	所有其他端点都需要经过身份验证的用户
4	未经身份验证的用户将被重定向到主页

# 如何获取访问令牌 #
现在可以从我们的新授权服务器访问令牌。获取令牌的最简单方法是抓取一个作为"极致"客户端。你可以看到这个，如果你运行应用程序并卷曲它：

	$ curl acme:acmesecret@localhost:8080/oauth/token -d grant_type=client_credentials
	{"access_token":"370592fd-b9f8-452d-816a-4fd5c6b4b8a6","token_type":"bearer","expires_in":43199,"scope":"read write"}
客户端证书令牌在某些情况下很有用(例如测试令牌端点的工作情况)，但要利用我们服务器的所有功能，我们希望能够为用户创建令牌。为了代表我们应用的用户获取令牌，我们需要能够对用户进行身份验证。如果您在应用程序启动时仔细观察日志，您会看到默认的Spring Boot用户会记录一个随机密码(根据Spring Boot用户指南)。您可以使用此密码代表ID为"user"的用户获取令牌：

	$ curl acme:acmesecret@localhost:8080/oauth/token -d grant_type=password -d username=user -d password=...
	{"access_token":"aa49e025-c4fe-4892-86af-15af2e6b72a2","token_type":"bearer","refresh_token":"97a9f978-7aad-4af7-9329-78ff2ce9962d","expires_in":43199,"scope":"read write"}

其中"..."应该用实际密码替换。这称为"密码"授权，您可以在其中交换访问令牌的用户名和密码。

密码授权也主要用于测试，但可以适用于本地或移动应用程序，当您拥有本地用户数据库来存储和验证证书时。对于大多数应用程序或任何具有"社交"登录的应用程序，如我们的应用程序，您需要"授权代码"授权，这意味着您需要一个浏览器(或者像浏览器一样的客户端)来处理重定向和Cookie以及呈现来自外部提供者的用户界面。

创建客户端应用程序
我们的Authorization Server客户端应用程序本身就是一个Web应用程序，使用Spring Boot很容易创建。这是一个例子：

	ClientApplication.java
	@EnableAutoConfiguration
	@Configuration
	@EnableOAuth2Sso
	@RestController
	public class ClientApplication {
	
	  @RequestMapping("/")
	  public String home(Principal user) {
	    return "Hello " + user.getName();
	  }
	
	  public static void main(String[] args) {
	    new SpringApplicationBuilder(ClientApplication.class)
	        .properties("spring.config.name=client").run(args);
	  }
	
	}

的ClientApplication类不能在同一包(或子包)来创建SocialApplication类。否则，Spring会在启动服务器ClientApplication时加载一些自动配置SocialApplication，导致启动错误。
客户端的组件是一个主页(只是输出用户的名字)，以及配置文件的明确名称(通过spring.config.name=client)。当我们运行这个应用程序时，它会查找我们提供的配置文件，如下所示：

	client.yml
	server:
	  port: 9999
	  context-path: /client
	security:
	  oauth2:
	    client:
	      client-id: acme
	      client-secret: acmesecret
	      access-token-uri: http://localhost:8080/oauth/token
	      user-authorization-uri: http://localhost:8080/oauth/authorize
	    resource:
	      user-info-uri: http://localhost:8080/me

配置看起来很像我们在主应用程序中使用的值，但使用"acme"客户端而不是Facebook或Github。该应用程序将运行在9999端口，以避免与主应用程序发生冲突。它指的是我们还没有实现的用户信息端点"/我"。

请注意，这server.context-path是明确设置的，所以如果您运行应用程序进行测试，请记住主页是http：// localhost：9999 / client。点击该链接应该将您带到auth服务器，一旦您通过您选择的社交提供商进行身份验证，您将被重定向回客户端应用程序

如果您在本地主机上同时运行客户端和auth服务器，则上下文路径必须是明确的，否则cookie路径冲突和两个应用程序无法就会话标识符达成一致。
保护用户信息端点
要使用我们的新授权服务器进行单点登录，就像我们已经使用Facebook和Github一样，它需要有一个/user由其创建的访问令牌保护的端点。到目前为止，我们有一个/user端点，它用用户验证时创建的cookie进行安全保护。为了保护它以及本地授予的访问令牌，我们可以重新使用现有端点并在新路径上为其创建别名：

	SocialApplication.java
	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("name", principal.getName());
	  return map;
	}

我们已将其转换Principal为a Map以隐藏我们不想公开给浏览器的部分，并且也解除了两个外部认证提供者之间端点的行为。原则上，我们可以在此处添加更多详细信息，例如特定于提供商的唯一标识符，或者可用的电子邮件地址。
通过声明我们的应用程序是资源服务器(以及授权服务器)，现在可以通过访问令牌来保护"/ me"路径。我们创建一个新的配置类(在主应用程序中作为n内部类，但它也可以拆分为独立的独立类)：

	SocialApplication.java
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration
	    extends ResourceServerConfigurerAdapter {
	  @Override
	  public void configure(HttpSecurity http) throws Exception {
	    http
	      .antMatcher("/me")
	      .authorizeRequests().anyRequest().authenticated();
	  }
	}

另外我们需要@Order为主应用程序安全性指定一个：

	SocialApplication.java
	@SpringBootApplication
	...
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	public class SocialApplication extends WebSecurityConfigurerAdapter {
	  ...
	}

该@EnableResourceServer注释@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER-1)默认创建一个安全筛选器，因此通过移动主应用程序安全性来@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)确保"/ me"规则优先。

测试OAuth2客户端
要测试新功能，您只需运行这两个应用程序并在浏览器中访问http：// localhost：9999 / client。客户端应用程序将重定向到本地授权服务器，然后授权用户通过Facebook或Github进行身份验证。完成后，控制权返回给测试客户端，授予本地访问令牌并完成身份验证(您应该在浏览器中看到"Hello"消息)。如果您已经通过Github或Facebook进行身份验证，您甚至可能不会注意到远程身份验证。

为未经身份验证的用户添加错误页面
在本节中，我们修改了我们之前构建的注销应用程序，切换到Github身份验证，并向无法进行身份验证的用户提供一些反馈。同时，我们借此机会扩展身份验证逻辑以包含一条规则，即只有当用户属于特定的Github组织时才允许用户使用该规则。"组织"是一个Github领域特定​​的概念，但是可以为其他提供者设计类似的规则，例如，对于Google，您可能只想验证来自特定域的用户身份。

切换到Github
该注销样品使用Facebook作为OAuth2用户提供。通过更改本地配置，我们可以轻松切换到Github：

	application.yml
	security:
	  oauth2:
	    client:
	      clientId: bd1c0a783ccdd1c9b9e4
	      clientSecret: 1a9030fbca47a5b2c28e92f19050bb77824b5ad1
	      accessTokenUri: https://github.com/login/oauth/access_token
	      userAuthorizationUri: https://github.com/login/oauth/authorize
	      clientAuthenticationScheme: form
	    resource:
	      userInfoUri: https://api.github.com/user

检测客户端中的验证失败
在客户端，我们需要能够为无法进行身份验证的用户提供一些反馈。为了促进这一点，我们添加一个带有信息性消息的div：

	index.html
	<div class="container text-danger error" style="display:none">
	There was an error (bad credentials).
	</div>

该文本只有在显示"error"元素时才会显示，因此我们需要一些代码来执行此操作：

	index.html
	$.ajax({
	  url : "/user",
	  success : function(data) {
	    $(".unauthenticated").hide();
	    $("#user").html(data.userAuthentication.details.name);
	    $(".authenticated").show();
	  },
	  error : function(data) {
	    $("#user").html('');
	    $(".unauthenticated").show();
	    $(".authenticated").hide();
	    if (location.href.indexOf("error=true")>=0) {
	      $(".error").show();
	    }
	  }
	});

身份验证功能会在加载浏览器位置时检查浏览器位置，如果它找到其中包含"error = true"的URL，则会设置标志。

添加一个错误页面
为了支持客户端中的标志设置，我们需要能够捕获认证错误并使用查询参数中设置的标志重定向到主页。因此，我们需要一个端点，@Controller像这样的常规端点：

	SocialApplication.java
	@RequestMapping("/unauthenticated")
	public String unauthenticated() {
	  return "redirect:/?error=true";
	}

在示例应用程序中，我们将它放在主应用程序类中，现在它是一个@Controller(而不是@RestController)所以它可以处理重定向。我们需要的最后一件事是从未经身份验证的响应(HTTP 401，又名UNAUTHORIZED)映射到我们刚刚添加的"/ unauthenticated"端点：

	ServletCustomizer.java
	@Configuration
	public class ServletCustomizer {
	  @Bean
	  public EmbeddedServletContainerCustomizer customizer() {
	    return container -> {
	      container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthenticated"));
	    };
	  }
	}

(在示例中，这是为了简洁起见，在主应用程序中添加为嵌套类。)

在服务器中生成一个401
如果用户不能或不想用Github登录，那么Spring的安全响应已经有了401响应，所以如果你没有通过身份验证(例如通过拒绝令牌授权)，应用程序已经在运行。

为了使事情变得有点过分，我们将扩展身份验证规则以拒绝不在正确组织中的用户。使用Github API很容易找到更多关于用户的信息，因此我们只需将其插入认证过程的正确部分即可。幸运的是，对于这样一个简单的用例，Spring Boot提供了一个简单的扩展点：如果我们声明了一个@Bean类型AuthoritiesExtractor，它将用于构建经过身份验证的用户的权限(通常为"角色")。我们可以使用该钩子来断言用户处于正确的版图，并且如果不是，则抛出异常：

	SocialApplication.java
	@Bean
	public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
	  return map -> {
	    String url = (String) map.get("organizations_url");
	    @SuppressWarnings("unchecked")
	    List<Map<String, Object>> orgs = template.getForObject(url, List.class);
	    if (orgs.stream()
	        .anyMatch(org -> "spring-projects".equals(org.get("login")))) {
	      return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
	    }
	    throw new BadCredentialsException("Not in Spring Projects origanization");
	  };
	}

请注意，我们已将自动装配OAuth2RestOperations到此方法中，因此我们可以使用它代表经过身份验证的用户访问Github API。我们这样做，然后循环查找组织，寻找与"春季项目"(这是用于存储Spring开源项目的组织)相匹配的组织。如果您想成功进行身份验证，并且您不在Spring工程团队中，您可以在那里替换自己的价值。如果没有匹配，我们就会抛出BadCredentialsException，这被Spring Security拿起并转向401响应。

在OAuth2RestOperations具有作为一个bean被创建，以及(如春季启动1.4)，但是这是微不足道的，因为它的成分都是autowirable通过具有使用@EnableOAuth2Sso：

	@Bean
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new OAuth2RestTemplate(resource, context);
	}

显然，上面的代码可以推广到其他认证规则，其中一些适用于Github，另一些适用于其他OAuth2提供者。所有你需要的是OAuth2RestOperations提供者API的知识和一些知识。
结论
我们已经看到如何使用Spring Boot和Spring Security以很少的努力来构建多种样式的应用程序。贯穿所有示例的主题是使用外部OAuth2提供者的"社交"登录。最终的样本甚至可以用于"内部"提供这样的服务，因为它具有与外部提供商相同的基本功能。所有示例应用程序都可以很容易地扩展和重新配置以用于更具体的用例，通常只需更改配置文件即可。请记住，如果您使用自己的服务器中的示例版本来注册Facebook或Github(或类似)并获取您自己的主机地址的客户端凭据。并且记住不要将这些证书放在源代码管理中！