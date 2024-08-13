let statusList = [];
let statusTimeList = ["下單時間", "付款時間", "出貨時間", "完成時間", "取消時間"];
const radios = document.querySelectorAll('input[name="btnradio"]'); //狀態類別導覽列的每個input

const tbody = document.querySelector('.orderTable tbody');
const statusTitle = document.querySelector('thead th:nth-child(4)');

let totalElements = Number(document.querySelector('#totalEle #elements').innerText); //總訂單筆數
let totalPages = Number(document.querySelector('#totalEle #pages').innerText); //總頁數
const form = document.querySelector('.inputContainer form')
const searchInput = form.innerHTML; //保留原本搜尋表單內的input

// 取得當前網址的 URLSearchParams 對象
const urlParams = new URLSearchParams(window.location.search);

// 從 URLSearchParams 中取得特定參數的值
let keywordParam = urlParams.get('q');
let columnParam = urlParams.get('c');



//頁面載入後先綁定更新欄位、與頁數按鈕
document.addEventListener('DOMContentLoaded', async () => {
    try {
        await getStatusList();
        updateButtonEvent();
        pageButtonAddEvent();
        checkDisablePageButtons();
    } catch (error) {
        console.error('Initialization failed:', error);
    }
});


//綁定狀態類別導覽列
radios.forEach(radio => {
    radio.addEventListener('change', async function () {
        const selectedId = getSelectedRadio();
        await updatePage(1, selectedId);
        updatePageButtonsAndTotalEle(totalElements, totalPages);
    })
});

//換頁
document.querySelector('#firstPage').addEventListener('click', function() {
    const firstPageButton = document.querySelector('.nums .page-num:first-child');
    updatePage(1);
    changePagebuttons(firstPageButton);
})

document.querySelector('#previousPage').addEventListener('click', function() {
    const currentPageButton = document.querySelector('.page-num.active');
    if(Number(currentPageButton.innerText) > 1){
        const targetPage = currentPageButton.innerText - 1;
        const lastButton = currentPageButton.previousElementSibling;
        updatePage(targetPage);
        changePagebuttons(lastButton);
    }
})

function pageButtonAddEvent(){
    document.querySelectorAll('.page-num').forEach(button => {
        button.addEventListener('click', () => {
            const targetPageID = button.innerText;
            updatePage(targetPageID);
            changePagebuttons(button);
        })
    })
}

document.querySelector('#nextPage').addEventListener('click', function() {
    const currentPageButton = document.querySelector('.page-num.active');
    if(Number(currentPageButton.innerText) < totalPages){
        const targetPage = Number(currentPageButton.innerText) + 1;
        const nextButton = currentPageButton.nextElementSibling;
        updatePage(targetPage);
        changePagebuttons(nextButton);
    }
})

document.querySelector('#lastPage').addEventListener('click', function() {
    const lastPageButton = document.querySelector('.nums .page-num:last-child');
    updatePage(totalPages);
    changePagebuttons(lastPageButton);
})

//取得該頁的訂單資料&設定頁數按鈕
async function updatePage(pageID) {

// 取得當前網址的 URLSearchParams 對象
const urlParams = new URLSearchParams(window.location.search);

// 從 URLSearchParams 中取得特定參數的值
let keywordParam = urlParams.get('q');
let columnParam = urlParams.get('c');
    
    try {
        const response = await axios.get(`search/a?c=${columnParam}&q=${keywordParam}&p=${pageID}`);

        // 更新總元素和總頁數
        totalElements = response.data.page.totalElements;
        totalPages = response.data.page.totalPages;

        // 檢查資料是否存在
        if (response.data._embedded && response.data._embedded.orderList) {
            setTableData(response.data._embedded.orderList);
            updateButtonEvent();
        } else {
            Swal.fire({
                icon: 'warning',
                title: '目前沒有此狀態的訂單資料喔！'
            });
            tbody.innerHTML = "";
        }

        return response;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

async function getStatusList() {
    try {
        const response = await axios.get('statusList');
        statusList = response.data;
    } catch (error) {
        console.error('Error fetching status list:', error);
        throw error;
    }
};

//更新表格
function setTableData(data) {
    tbody.innerHTML = "";
    let row = "";
    data.forEach(order => {
        const chosenDate = chooseAndFormatDate(order);
        row += `
        <tr>
            <td><a href="detail?oid=${order.orderID}">${order.orderID}</a></td>
            <td><a href="detail?oid=${order.orderID}">${order.memberID}</a></td>
            <td><a href="detail?oid=${order.orderID}">${order.totalPrice}</a></td>
            <td><a href="detail?oid=${order.orderID}">${chosenDate}</a></td>
            <td><a href="detail?oid=${order.orderID}">${order.orderStatus.status}</a></td>
            <td class="modified"><i class="fa-solid fa-pen-to-square"></i></td>
        </tr>
        `
    })
    tbody.innerHTML = row;
};

//選擇要顯示的日&&格式化
function chooseAndFormatDate(orderData) {
    const statusID = orderData.orderStatus.statusID;
    let isoDate;
    switch (statusID) {
        case 0:
        case 1:
            isoDate = orderData.orderDate;
            break;
        case 2:
            isoDate = orderData.paymentDate;
            break;
        case 3:
            isoDate = orderData.shippingDate;
            break;
        case 4:
            isoDate = orderData.completeDate;
            break;
        case 5:
            isoDate = orderData.cancelDate;
            break;
    }

    const date = new Date(isoDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
};

//更新目前頁數按鈕
function changePagebuttons(button) {
    document.querySelector('.nums .active').classList.remove('active');
    button.classList.add('active');
    checkDisablePageButtons();
};

//依照資料數更新頁數按鈕列
function updatePageButtonsAndTotalEle(totalElements, totalPages){
    document.querySelector('#totalEle #elements').innerText = totalElements;
    document.querySelector('#totalEle #pages').innerText = totalPages;
    const pageButtons = document.querySelector('.nums');
    pageButtons.innerHTML = "";
    let buttonTemplate = `<button type="button" class="page-num active">1</button>`;
    for (let index = 1; index < totalPages; index++) {
        buttonTemplate += `
        <button type="button" class="page-num">${index + 1}</button>
        `;
    }
    pageButtons.innerHTML = buttonTemplate;
    pageButtonAddEvent();
    checkDisablePageButtons();
};

//依照頁碼禁用上一頁或下一頁按鈕
function checkDisablePageButtons() {
    const currentPage = Number(document.querySelector('.page-num.active').innerText);
    const isFirstPage = currentPage === 1;
    const isLastPage = currentPage === totalPages;
    const isSinglePageOrEmpty = totalPages <= 1;

    toggleButtonDisabled('#previousPage', isFirstPage || isSinglePageOrEmpty);
    toggleButtonDisabled('#firstPage', isFirstPage || isSinglePageOrEmpty);
    toggleButtonDisabled('#nextPage', isLastPage || isSinglePageOrEmpty);
    toggleButtonDisabled('#lastPage', isLastPage || isSinglePageOrEmpty);
};

function toggleButtonDisabled(selector, condition) {
    const button = document.querySelector(selector);
    if (condition) {
        button.setAttribute('disabled', true);
    } else {
        button.removeAttribute('disabled');
    }
};

let chosenOrderID;
let chosenRow;

//更新
function updateButtonEvent(){
    const complete = statusList.indexOf('已完成');
    const cancel = statusList.indexOf('已取消');
    document.querySelectorAll('.modified').forEach(element => {
        const status = element.previousElementSibling.innerText;
        const id = statusList.indexOf(status);

        if(id === complete || id === cancel){
            element.innerHTML = "";
        } else{
            console.log('再綁定');
            element.addEventListener('click', () => {
                chosenRow = element.closest('tr');
                chosenOrderID = chosenRow.querySelector('td:first-child a').innerText;
                showChangeStatusAlert();
            })
        }

    })
};
//執行更新的function
async function statusChangeAction(option) {
    const id = statusList.indexOf(option) + 1;
    axios.post('update', {
        orderID: chosenOrderID,
        statusID: id
    })
    .then(response => {
        const statusBlock = chosenRow.querySelector('td:nth-child(5) a');
        statusBlock.innerText = response.data.orderStatus.status;
        updateSuccessAlert();

        const currentPage = Number(document.querySelector('.page-num.active').innerText);
        updatePage(currentPage);
    })
    .catch(error => {
        console.error(error); 
    })
    
};