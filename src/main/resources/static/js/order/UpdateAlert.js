const confirmSwal = Swal.mixin({
	showDenyButton: true,
	showCancelButton: true,
	confirmButtonText: "確認更改",
	cancelButtonText: "取消更改",
	denyButtonText: "重新選擇",
	reverseButtons: true
});

//更改狀態按鈕的選單
function showChangeStatusAlert() {
	Swal.fire({
		icon: 'question',
		title: '請選擇要更改的訂單狀態',
		showConfirmButton: false,
		html: `
			<button id="created" class="swal2-custom-button" onclick="finalConfirm('已成立')">已成立</button>
			<button id="payment" class="swal2-custom-button" onclick="finalConfirm('已付款')">已付款</button>
			<button id="shipping" class="swal2-custom-button" onclick="finalConfirm('已出貨')">已出貨</button><br>
			<button id="complete" class="swal2-custom-button" onclick="finalConfirm('已完成')">已完成</button>
			<button id="cancel" class="swal2-custom-button" onclick="cancelConfirm('已取消')">已取消</button>
		`,
		showCancelButton: true,
		cancelButtonText: "取消",
		showClass: {
			popup: `
			animate__animated
			animate__fadeIn
			`
		},
		hideClass: {
			popup: `
			animate__animated
			animate__fadeOut
			animate__faster
			`
		}
	});
};

function finalConfirm(option) {
	confirmSwal.fire({
		title: `確定要更改為 <span class="swal-word">${option}</span> 嗎?`,
		showClass: {
			popup: `
			animate__animated
			animate__fadeIn
			`
		},
		hideClass: {
			popup: `
			animate__animated
			animate__fadeOut
			animate__faster
			`
		}
	}).then(result => {
		if(result.isConfirmed) {
			statusChangeAction(option);
		}else if(result.isDenied) {
			showChangeStatusAlert();
		}
	})
};

function cancelConfirm(option) {
	confirmSwal.fire({
		title: `確定要更改為 <span id="cancel-word">${option}</span> 嗎?`,
		footer: `<b id="cancel-word">取消此訂單無法再更改回其他狀態！請慎重考慮</b>`,
		showClass: {
			popup: `
			animate__animated
			animate__fadeIn
			`
		},
		hideClass: {
			popup: `
			animate__animated
			animate__fadeOut
			animate__faster
			`
		}
	}).then(result => {
		if(result.isConfirmed) {
			statusChangeAction(option);
		}else if(result.isDenied) {
			showChangeStatusAlert();
		}
	})
};

function updateSuccessAlert() {
	Swal.fire({
		icon: 'success',
		title: '狀態修改成功',
		timer: 2000,
		timerProgressBar: true,
		toast: true,
		showConfirmButton: false,
		position: 'top'
	})
}