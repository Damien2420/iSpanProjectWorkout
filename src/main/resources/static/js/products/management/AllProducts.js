const bodyRows = document.querySelectorAll('tbody tr');
const deleteBtn = document.querySelector('.delete');

const buttons = document.querySelector('.buttons');
const originalButtonContent = buttons.innerHTML;
let totalElements = Number(document.querySelector('.totalmsg #elements').innerText); //總訂單筆數
let totalPages = Number(document.querySelector('.totalmsg #pages').innerText); //總頁數
const deleteContent = `
		<div class="col d-flex justify-content-center align-items-center">
			<button class="functionBtn delete">
				<i class="fa-solid fa-xmark"></i> 取消刪除
			</button>
		</div>
		<div class="col d-flex justify-content-center align-items-center">
			<button class="functionBtn delete-confirmed">
				<i class="fa-solid fa-trash-can"></i> 確認刪除
			</button>
		</div>
		`;

// 進入畫面執行
document.addEventListener('DOMContentLoaded', function () {
    toggleDelete();
    cancelDeleteEvent(deleteBtn);
    checkDisablePageButtons();
    pageButtonAddEvent();
    updateLaunchEvent();
});

// 監聽表格變化以顯示刪除按鈕
const observer = new MutationObserver(toggleDelete);
observer.observe(document.querySelector('tbody'), { childList: true });

document.querySelectorAll('.delete-column input[type="checkbox"]').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateButtonState);
});

// 如果沒有勾選就disable確認按鈕
function updateButtonState() {
    let anyChecked = document.querySelectorAll('.delete-column input[type="checkbox"]:checked').length > 0;
    document.querySelector('.delete-confirmed').disabled = !anyChecked;
}

// 沒有資料就不顯示刪除按鈕
function toggleDelete() {
    let rowCount = bodyRows.length;
    if (rowCount === 0) {
        deleteBtn.style.display = 'none';
    } else {
        deleteBtn.style.display = 'block';
    }
}

// 取消勾選刪除checkbox
function uncheck() {
    const deleteCheckbox = document.querySelectorAll('input[name="delete"]:checked');
    deleteCheckbox.forEach(checkbox => {
        checkbox.checked = false;
    });
}

// 刪除列與功能按鈕控制
let isDeleteFuncActive = false;
function deleteRow() {
    if (!isDeleteFuncActive) {
        buttons.innerHTML = deleteContent;
        document.querySelectorAll('.delete-column').forEach(function (element) {
            element.style.display = 'table-cell';
        })
        const deleteButton = document.querySelector('.delete');
        cancelDeleteEvent(deleteButton);
        deleteButton.style.backgroundColor = '#13AE85';
        const deleteConfirmButton = document.querySelector('.delete-confirmed');
        deleteConfirmButton.addEventListener('click', () => {
            deleteAlert();
        });
    } else {
        buttons.innerHTML = originalButtonContent;
        const deleteButton = document.querySelector('.delete');
        deleteButton.style.backgroundColor = '#DC143C';
        cancelDeleteEvent(deleteButton);
        document.querySelectorAll('.delete-column').forEach(function (element) {
            element.style.display = 'none';
        })
    }
    isDeleteFuncActive = !isDeleteFuncActive;
}


// 觸發刪除列的監聽事件
function cancelDeleteEvent(button) {
    button.addEventListener('click', () => {
        deleteRow();
        uncheck();
    });
}

function updateLaunchEvent() {
    document.querySelectorAll('.launch-status input[type="checkbox"]').forEach(function (checkbox) {
        checkbox.addEventListener('change', () => {
            const launchStatus = Boolean(checkbox.checked);
            const row = checkbox.closest('tr');
            const productID = row.querySelector('td:first-child a').innerText.trim();

            axios.put(`updateLaunch/${productID}?status=${launchStatus}`)
            .catch(err => {
                console.error(err); 
            })
        })
    });
};

//換頁
document.querySelector('#firstPage').addEventListener('click', function () {
    const firstPageButton = document.querySelector('.nums .page-num:first-child');
    updatePage(1);
    changePagebuttons(firstPageButton);
});

document.querySelector('#previousPage').addEventListener('click', function () {
    const currentPageButton = document.querySelector('.page-num.active');
    if (Number(currentPageButton.innerText) > 1) {
        const targetPage = currentPageButton.innerText - 1;
        const lastButton = currentPageButton.previousElementSibling;
        updatePage(targetPage);
        changePagebuttons(lastButton);
    }
});

function pageButtonAddEvent() {
    document.querySelectorAll('.page-num').forEach(button => {
        button.addEventListener('click', () => {
            const targetPageID = button.innerText;
            updatePage(targetPageID);
            changePagebuttons(button);
        })
    })
};

document.querySelector('#nextPage').addEventListener('click', function () {
    const currentPageButton = document.querySelector('.page-num.active');
    if (Number(currentPageButton.innerText) < totalPages) {
        const targetPage = Number(currentPageButton.innerText) + 1;
        const nextButton = currentPageButton.nextElementSibling;
        updatePage(targetPage);
        changePagebuttons(nextButton);
    }
});

document.querySelector('#lastPage').addEventListener('click', function () {
    const lastPageButton = document.querySelector('.nums .page-num:last-child');
    updatePage(totalPages);
    changePagebuttons(lastPageButton);
});

//頁碼按鈕
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

function changePagebuttons(button) {
    document.querySelector('.nums .active').classList.remove('active');
    button.classList.add('active');
    checkDisablePageButtons();
};

function formatDate(productData) {
    const isoDate = productData.addedDate;
    const date = new Date(isoDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}年${month}月${day}日`;
};

function updatePage(pageID){
    axios.get(`allList/${pageID}`)
    .then(response => {
        totalElements = response.data.page.totalElements;
        totalPages = response.data.page.totalPages;
        const table = document.querySelector('table tbody');
        table.innerHTML = "";
        response.data._embedded.productList.forEach(product => {
            const date = formatDate(product);
            let row = `
            <tr>
                <td><a href="fullDetail?productID=${product.productID}">${product.productID}</a></td>
                <td><a href="fullDetail?productID=${product.productID}">${product.productCategory.categoryName}</a></td>
                <td><a href="fullDetail?productID=${product.productID}">${product.productName}</a></td>
                <td><a href="fullDetail?productID=${product.productID}">${product.price}</a></td>
                <td><a href="fullDetail?productID=${product.productID}">${product.stock}</a></td>
                <td><a href="fullDetail?productID=${product.productID}">${date}</a></td>
            `
            if(product.launchStatus) {
                row += `
                <td class="launch-status">
                    <label class="switch">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </td>
                `;
            }else {
                row += `
                <td class="launch-status">
                    <label class="switch">
                        <input type="checkbox">
                        <span class="slider"></span>
                    </label>
                </td>
                `;
            };
            row += `
                <td>
                    <a href="updateInfo?productID=${product.productID}" id="modified">
                        <i class="fa-solid fa-wrench"></i>
                    </a>
                </td>
                <td class="delete-column">
                    <input type="checkbox" class="ui-checkbox" name="delete" id="delete-checkbox" value="${product.productID}">
                </td>
            </tr>
            `;
            table.innerHTML += row;
        })
        updateLaunchEvent();
    })
    .catch(err => {
        console.error(err); 
    })
}