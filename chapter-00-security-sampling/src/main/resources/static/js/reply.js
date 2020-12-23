const replyManager = (function() {
	const getAll = function(obj, callback) {
		console.log("get All....");
		$.getJSON(`/replies/${obj}`, callback);
	};
	
	const add = function(obj, callback) {
		console.log("add...");
		console.log(JSON.stringify(obj));
		$.ajax({
			type:'post',
			url: `/replies/${obj.bno}`,
			data: JSON.stringify(obj),
			dataType:'json',
			contentType: "application/json; charset=UTF-8",
			success: callback
		});
	};
	
	const update = function(obj, callback) {
		console.log("update...");
		$.ajax({
			type:'put',
			url: `/replies/${obj.bno}`,
			data: JSON.stringify(obj),
			dataType:'json',
			contentType: "application/json; charset=UTF-8",
			success: callback
		});
	};
	
	const remove = function(obj, callback) {
		$.ajax({
			type:'delete',
			url: `/replies/${obj.bno}/${obj.rno}`,
			dataType:'json',
			contentType: "appliction/json",
			success: callback
		});
	};
	
	return {
		getAll: getAll,
		add: add,
		update: update,
		remove: remove
	};
})();