

function show4Permissions() {
	url = '/rest/feign/auth/logins/users/permissions/current';
	var pers = [];
	$.ajax({
		type : 'GET',
		url : url,
		contentType : "application/json; charset=utf-8",
		dataType: "json",
		success : function(result) {
  			//alert(JSON.stringify(data));
			handleResultCode(result, function (result) {
				pers = result.data;
				if (pers==null){
					//alert("per null");
				} else if (pers.length==1&&pers[0]=="*allpass*"){
					//alert(pers[0]);
					$("[permission]").each(function() {
						var per = $(this).attr("permission");
						$(this).show(0);
						$(this).removeClass("disabled");
					});
				} else {
					$("[permission]").each(function() {
						var per = $(this).attr("permission");
						//alert(per);
						//alert($.inArray(per, pers));
						if ($.inArray(per, pers) >= 0) {
							$(this).show(0);
							$(this).removeClass("disabled");
						}
					});
				}
			});
	
		}
	});
	return pers;
}
function show4CurrentUser(){
	$.ajax({
		type: "GET",
		url: '/rest/feign/auth/logins/users/current',
		contentType: "application/json;charset=utf-8",
		dataType: "json",
		success:function (result) {
			handleResultCode(result, function (result) {
				//$("#userinfohtml").html(result.data.userName);
				document.getElementById("headeruserinfo").innerText="["+result.data.userName+"]";
				document.getElementById('headerlogoutlink').style.display='block';	
			});
			if (result.code!="1"){
				document.getElementById('headerloginlink').style.display='block';	
			}
		}
	});
}


//bootstrap pagination
function createPaginationHtml(totalpages,totalelements,currentpage,topageaction){
	var ret = "";
	ret = ret + "<ul class='pagination'><li><span>Total:&nbsp;"+totalelements+"</span></li><li><a onclick='"+topageaction+"(0)'>|&lt;</a></li>";
	
	if (currentpage=='0'){
		ret = ret + "<li class='disabled'><span>&laquo;</span></li>";
	} else {
		ret = ret + "<li><a onclick='"+topageaction+"("+(currentpage-1)+")'>&laquo;</a></li>";
	}
	var displaypagecount=9;
	var front="";
	var middle="";
	var end="";
	var offset=0;
	var displaypages=totalpages;
	if (totalpages>displaypagecount){
		displaypages = displaypagecount;
		if (currentpage>=(displaypagecount-1)){
			offset = currentpage - displaypagecount + 2;
			front = "<li ><span>...</span></li>";
		}
		if (currentpage<(displaypagecount-1)){
			offset = 0;
		}
		if (currentpage<(totalpages-2)){
			end = "<li ><span>...</span></li>";
		}
		if(offset>(totalpages-displaypagecount)){
			offset=totalpages-displaypagecount;
		}
	}
	for (var i = 0; i < displaypages; i++) {
		var j = offset+i;
		if (j==currentpage){
			middle = middle + "<li class='active' ><a onclick='"+topageaction+"("+j+")'>"+(j+1)+"</a></li>";
		} else {
			middle = middle + "<li><a onclick='"+topageaction+"("+j+")'>"+(j+1)+"</a></li>";
		}
	}
	ret = ret + front + middle + end;
	if (currentpage==(totalpages-1)){
		ret = ret + "<li class='disabled'><span>&raquo;</span></li>";
	} else {
		ret = ret + "<li><a onclick='"+topageaction+"("+(currentpage+1)+")'>&raquo;</a></li>"; 
	}
	ret = ret + "<li><a onclick='"+topageaction+"("+(totalpages-1)+")'>&gt;|</a></li></ul>";
	return ret;
}


function ieversion() {
    // 取得浏览器的userAgent字符串
    var userAgent = navigator.userAgent;
    // 判断是否为小于IE11的浏览器
    var isLessIE11 = userAgent.indexOf('compatible') > -1 && userAgent.indexOf('MSIE') > -1;
    // 判断是否为IE的Edge浏览器
    var isEdge = userAgent.indexOf('Edge') > -1 && !isLessIE11;
    // 判断是否为IE11浏览器
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf('rv:11.0') > -1;
    if (isLessIE11) {
        var IEReg = new RegExp('MSIE (\\d+\\.\\d+);');
        // 正则表达式匹配浏览器的userAgent字符串中MSIE后的数字部分，，这一步不可省略！！！
        IEReg.test(userAgent);
        // 取正则表达式中第一个小括号里匹配到的值
        var IEVersionNum = parseFloat(RegExp['$1']);
        if (IEVersionNum === 7) {
            // IE7
            return 7
        } else if (IEVersionNum === 8) {
            // IE8
            return 8
        } else if (IEVersionNum === 9) {
            // IE9
            return 9
        } else if (IEVersionNum === 10) {
            // IE10
            return 10
        } else {
            // IE版本<7
            return 6
        }
    } else if (isEdge) {
        // edge
        return 'edge'
    } else if (isIE11) {
        // IE11
        return 11
    } else {
        // 不是ie浏览器
        return -1
    }
}


