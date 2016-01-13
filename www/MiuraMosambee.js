var exec = require('cordova/exec');

exports.sale = function(amount,orderid,username,password, success, error) {
	var arg = {
		amount:amount,
		orderid:orderid,
		username:username,
		password:password
	}
    exec(success, error, "MiuraMosambee", "sale", [arg]);
};
