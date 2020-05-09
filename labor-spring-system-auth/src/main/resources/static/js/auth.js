var RESTURL_AUTH_PROFILE_USERS =  		"http://47.106.74.136/auth/rest/profiles/users";
var HTMLURL_AUTH_PROFILE_USERS_INFO =  	"http://47.106.74.136/auth/profile/user-info";
var HTMLURL_AUTH_SSO_LOGIN =  			"http://47.106.74.136/auth/sso/login";
var HTMLURL_AUTH_SSO_LOGOUT =  			"http://47.106.74.136/auth/sso/logout";

function gotoUserInfoPage(){
//	window.location.href = HTMLURL_AUTH_PROFILE_USERS_INFO;	
	window.open(HTMLURL_AUTH_PROFILE_USERS_INFO);
}
