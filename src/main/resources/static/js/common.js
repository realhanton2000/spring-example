function ajax_action(httpType, httpUrl, successMsg, callback){
    $.ajax({
		type: httpType,
		contentType: "application/json",
		url: httpUrl,
		timeout: 600000,
		success: function(data){
			var success = successMsg;
			var error = "";
			var json = "";
			if (data){
			    json = JSON.stringify(data);
            }
			$("div#success").html(success);
			$("div#error").html(error);
			$("div#json").html("<pre id='json'>" + json + "</pre>");
			$("div#success").show();
			$("div#error").hide();
			if (data){
			    $("div#json").show();
			} else{
			    $("div#json").hide();
			}
			if (json && callback){
			    callback(json);
			}
		},
		error: function(error) {
			console.log("ERROR: ", error);
			var success = "";
			var error = JSON.parse(error.responseText).message;
			var json = "";
			$("div#success").html(success);
			$("div#error").html(error);
			$("div#json").html(json);
			$("div#success").hide();
			$("div#error").show();
			$("div#json").hide();
		}
	});
}

function ajax_action_data(httpType, httpUrl, httpData, successMsg, callback){
    $.ajax({
		type: httpType,
		contentType: "application/json",
		url: httpUrl,
		data: httpData,
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function(data) {
			var success = successMsg;
			var error = "";
			var json = "";
			if (data){
            	json = JSON.stringify(data);
            }
			$("div#success").html(success);
			$("div#error").html(error);
			$("div#json").html("<pre id='json'>" + json + "</pre>");
			$("div#success").show();
			$("div#error").hide();
			if (data){
                $("div#json").show();
            } else{
            	$("div#json").hide();
            }
            if (json && callback){
                callback(json);
            }
		},
		error: function(error) {
			console.log("ERROR: ", error);
			var success = "";
			var error = JSON.parse(error.responseText).message;
			var json = "";
			$("div#success").html(success);
			$("div#error").html(error);
			$("div#json").html(json);
			$("div#success").hide();
			$("div#error").show();
			$("div#json").hide();
		}
	});
}

function clear_response_div(){
     $("div#success").html("");
     $("div#error").html("");
     $("div#json").html("");
     $("div#success").hide();
     $("div#error").hide();
     $("div#json").hide();
}