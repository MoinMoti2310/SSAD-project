/*
 * Scanon Maps
 * asId = Action Set ID
 * showInfoWindow = Display Info Window on load
 */
(function($) {
	$.fn
			.extend({
				// To call this type of alerts
				// $('#alert-container').bs_info(message, 'SUCCESS');
				bs_alert : function(message, title) {
					var cls = 'alert-danger';
					var html = '<div class="alert '
							+ cls
							+ ' alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>';
					if (typeof title !== 'undefined' && title !== '') {
						// html+='<h4>'+title+'</h4>';
					}
					html += '<span>' + message + '</span></div>';
					$(this).html(html);
				},
				bs_warning : function(message, title) {
					var cls = 'alert-warning';
					var html = '<div class="alert '
							+ cls
							+ ' alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>';
					if (typeof title !== 'undefined' && title !== '') {
						// html+='<h4>'+title+'</h4>';
					}
					html += '<span>' + message + '</span></div>';
					$(this).html(html);
				},
				bs_info : function(message, title) {
					var cls = 'alert-info';
					var html = '<div class="alert '
							+ cls
							+ ' alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>';
					if (typeof title !== 'undefined' && title !== '') {
						// html+='<h4>'+title+'</h4>';
					}
					html += '<span>' + message + '</span></div>';
					$(this).html(html);
				}
			});
})(jQuery);

function options(){
	
	toastr.options = {
			"closeButton" : true,
			"debug" : false,
			"newestOnTop" : true,
			"progressBar" : false,
			"positionClass" : "toast-top-center",
			"preventDuplicates" : true,
			"onclick" : null,
			"showDuration" : "300",
			"hideDuration" : "1000",
			"timeOut" : "5000",
			"extendedTimeOut" : "1000",
			"showEasing" : "swing",
			"hideEasing" : "linear",
			"showMethod" : "fadeIn",
			"hideMethod" : "fadeOut"
		}
}

function showMessage(message, title, type) {
	
	options();

	if (!(typeof message !== 'undefined' && message !== '')) {
		title = 'Error occured';
		message = 'Please contact support';
	}
	if (typeof title !== 'undefined' && title !== '') {
		toastr[type](message, title);
	} else {
		toastr[type](message);
	}
}