const statusWords = ['訂單成立時間 : ', '付款時間 : ', '出貨時間 : ', '完成時間 : ', '取消時間 : ']; //狀態敘述標題
const dates = {}; //裝axios取得的日期資料
let statusList = []; //裝axios取得的狀態列表

const orderID = document.querySelector('#id-title').textContent.slice(-12); //訂單ID
const statusEle = document.querySelector('.order-status span'); //狀態ID元素
const statusID = statusEle.getAttribute('data-status');

const progressCircle = document.querySelectorAll('.steps .circle'); //進度條節點(circle)

const updateButton = document.querySelector('#change-status-btn');

const statusTimeDescription = document.querySelector('.status-desc'); //狀態時間敘述
const statusTime = document.querySelector('.time'); //狀態時間
//保留目前訂單狀態
let currentStatusID;
let currentStatusDate;

//取得日期
document.addEventListener('DOMContentLoaded', () => {
	getOrderStatusAjax(orderID);
	getStatusList();
	updateButtonControl(statusID);
	//進入頁面初始進度條與說明文字
	changeStatus(statusID);
	statusTimeDescription.innerText = statusWords[statusID - 1];
	
});

//進度條節點說明文字變更事件綁定
progressCircle.forEach((circle, index) => {
	circle.addEventListener('mouseenter', () => {
		orderDateSelector(index);
	})
	circle.addEventListener('mouseleave', () => {
		statusTimeDescription.innerText = statusWords[currentStatusID - 1];
		statusTime.innerText = currentStatusDate;
	})
})

function orderDateSelector(index) {
	switch (index) {
		case 0:
			statusTimeDescription.innerText = statusWords[index];
			statusTime.innerText = dates.orderDate;
			break;
		case 1:
			statusTimeDescription.innerText = statusWords[index];
			statusTime.innerText = dates.paymentDate;
			break;
		case 2:
			statusTimeDescription.innerText = statusWords[index];
			statusTime.innerText = dates.shippingDate;
			break;
		case 3:
			statusTimeDescription.innerText = statusWords[index];
			statusTime.innerText = dates.completeDate;
			break;
		case 4:
		statusTimeDescription.innerText = statusWords[index];
		statusTime.innerText = dates.cancelDate;
	}
}

//狀態修改的按鈕顯示設定
function updateButtonControl(statusID){
	const numericrStatusID = Number(statusID);
	if(numericrStatusID == 4) {
		updateButton.style.display = 'none';
	}else if(numericrStatusID < 4) {
		updateButton.addEventListener('click', event => {
		if (event.target.closest('button')) {
				showChangeStatusAlert();
			}
		});
	};
}

//格式化日期
function formatDate(isoDate) {
	const date = new Date(isoDate);
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');
	const seconds = String(date.getSeconds()).padStart(2, '0');
	return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
};

//日期資料請求
function getOrderStatusAjax(orderID) {
	axios.get('date', {
		params: { id: orderID }
	})
	.then(response => {
		buildDatesObject(response.data);
		setOriginalStatus(statusID);
		orderDateSelector(statusID - 1);
	})
	.catch(error => {
		console.error(error);
	});
};

function buildDatesObject(data) {
	dates.orderDate = formatDate(data.orderDate);
	dates.paymentDate = data.paymentDate === null ? "此訂單還未付款" : formatDate(data.paymentDate);
	dates.shippingDate = data.shippingDate === null ? "此訂單還未出貨" : formatDate(data.shippingDate);
	dates.completeDate = data.completeDate === null ? "此訂單還未完成" : formatDate(data.completeDate);
	dates.cancelDate = data.cancelDate === null ? "此訂單並沒有取消喔" : formatDate(data.cancelDate);
}

//狀態資料請求
function getStatusList() {
	axios.get('statusList')
	.then(response => {
		statusList = response.data;
	})
	.catch(error => {
		console.error(error); 
	})
}

//依照狀態調整進度條
function changeStatus(statusID) {
	const numericID = Number(statusID);
	const progressBar = document.querySelector('.progress-bar'); //BS進度條

	progressCircle.forEach(circle => { circle.classList.remove('status-active')});

	for (let i = 0; i < numericID; i++) {
		if (progressCircle[i]) {
			progressCircle[i].classList.add('status-active');
		}
	}

	switch (numericID) {
		case 1:
			progressBar.style.width = "7%";
			break;
		case 2:
			progressBar.style.width = "37%";
			break;
		case 3:
			progressBar.style.width = "67%";
			break;
		case 4:
			progressBar.style.width = "100%";
			break;
	}
};

//設定原本紀錄的訂單狀態&時間
function setOriginalStatus(id) {
	const numericID = Number(id);
	currentStatusID = numericID;
	switch(numericID) {
		case 1:
			currentStatusDate = dates.orderDate;
			break;
		case 2:
			currentStatusDate = dates.paymentDate;
			break;
		case 3:
			currentStatusDate = dates.shippingDate;
			break;
		case 4:
			currentStatusDate = dates.completeDate;
			break;
		case 5:
			currentStatusDate = dates.cancelDate;
			break;
	}
};

function statusChangeAction(option) {
	const id = statusList.indexOf(option) + 1;

	axios.post('update', {
		orderID: orderID,
		statusID: id
	})
	.then(response => {
		if(id === 5) {
			const htmlTemplate = `
			<div class="steps justify-content-center">
				<span class="cancel-circle"><i class="fa-solid fa-rectangle-xmark"></i></span>
			</div>
			`;
			document.querySelector('.progress-nav div').innerHTML = htmlTemplate;
			statusEle.innerText = "已取消";
			statusEle.id = 'cancel-word';
			updateButton.style.display = 'none';
		}else if(id >= 1 && id < 5) {
			changeStatus(id);
			statusEle.innerText = statusList[id - 1];
			updateButtonControl(id);
		}
		updateSuccessAlert();
		buildDatesObject(response.data);
		setOriginalStatus(id);
		orderDateSelector(id - 1);
	})
	.catch(error => {
		console.error(error); 
	})
};