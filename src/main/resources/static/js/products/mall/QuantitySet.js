$(function() {
	const add = $('.add');
	const minus = $('.minus');
	let action;

	function increase(quantityBox) {
		let value = parseInt(quantityBox.val());
		quantityBox.val(value + 1);
	}

	function decrease(quantityBox) {
		let value = parseInt(quantityBox.val());
		if (value > 0) {
			quantityBox.val(value - 1);
		}
	}

	function startIncrease(event, quantityBox) {
		event.preventDefault();
		action = setInterval(function(event) {
			increase(quantityBox);
		}, 500);
	}

	function startDecrease(event, quantityBox) {
		event.preventDefault();
		action = setInterval(function(event) {
			decrease(quantityBox);
		}, 500);
	}

	function stop(event) {
		event.preventDefault();
		clearInterval(action);
	}

	add.on('click', function(event) {
		event.preventDefault();
		let quantityBox = $(this).siblings('.number');
		increase(quantityBox);
	});
	minus.on('click', function(event) {
		event.preventDefault();
		let quantityBox = $(this).siblings('.number');
		decrease(quantityBox);
	});

	add.on('mousedown', function(event) {
		let quantityBox = $(this).siblings('.number');
		startIncrease(event, quantityBox);
	}).on('mouseup', function(event) {
		stop(event);
	});

	minus.on('mousedown', function(event) {
		let quantityBox = $(this).siblings('.number');
		startDecrease(event, quantityBox);
	}).on('mouseup', function(event) {
		stop(event);
	});
})