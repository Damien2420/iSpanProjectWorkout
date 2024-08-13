//搜尋條件下拉式選單
const root = document.documentElement;
const dropdownTitle = document.querySelector(".dropdown-title");
const dropdownList = document.querySelector(".dropdown-list");
const mainButton = document.querySelector(".main-button");
const listItems = ["訂單編號", "會員編號", "下單日期", "付款日期", "出貨日期", "完成日期", "取消日期"];
const itemReprsentation = ["orderID", "memberID", "orderDate", "paymentDate", "shippingDate", "completeDate", "cancelDate"];

const listItemTemplate = (text, translateValue, index) => {
    return `
        <li class="dropdown-list-item">
        <button class="dropdown-button list-button" data-translate-value="${translateValue}%" data-r-value="${itemReprsentation[index]}">
            <span class="text-truncate">${text}</span>
        </button>
        </li>
    `;
};

const renderListItems = () => {
    dropdownList.innerHTML += listItems
        .map((item, index) => {
            return listItemTemplate(item, 100 * index, index);
        })
        .join("");
};

window.addEventListener("load", () => {
    renderListItems();
    setDataAttrEvent();
});

const setDropdownProps = (deg, ht, opacity) => {
    root.style.setProperty("--rotate-arrow", deg !== 0 ? deg + "deg" : 0);
    root.style.setProperty("--dropdown-height", ht !== 0 ? ht + "rem" : 0);
    root.style.setProperty("--list-opacity", opacity);
};

mainButton.addEventListener("click", () => {
    const dropdownOpenHeight = 3.1 * listItems.length;
    const currDropdownHeight =
        root.style.getPropertyValue("--dropdown-height") || "0";

    currDropdownHeight === "0"
        ? setDropdownProps(180, dropdownOpenHeight, 1)
        : setDropdownProps(0, 0, 0);
});

dropdownList.addEventListener("mouseover", (e) => {
    const translateValue = e.target.dataset.translateValue;
    root.style.setProperty("--translate-value", translateValue);
});

dropdownList.addEventListener("click", (e) => {
    const clickedItemText = e.target.innerText.toLowerCase().trim();
    dropdownTitle.innerHTML = clickedItemText;
    setDropdownProps(0, 0, 0);
});

//設定Java欄位名稱到屬性
function setDataAttrEvent(){
    document.querySelectorAll('.dropdown-button').forEach(button => {
        button.addEventListener('click', function() {
            const columnValue = this.dataset.rValue;
            mainButton.dataset.cv = columnValue;
        })
    })
}

let column = mainButton.dataset.cv;

// 變動回調函數
const observer = new MutationObserver(() => {
    column = mainButton.dataset.cv;
    if(column.includes('Date')){
        form.innerHTML = `<input type="date" name="q">`;
    } else if(column.includes('memberID')){
        document.querySelector('#inputField').placeholder = "請輸入完整的8碼會員編號";
    } else{
        form.innerHTML = searchInput;
    }

});

// 開始監聽目標元素的變動
observer.observe(dropdownTitle, { childList: true });

//送出查詢表單
document.querySelector('#searchBtn').addEventListener('click', () => {
    const keyword = document.querySelector('.inputContainer form input').value;
    if(column.includes('condition')){
        Swal.fire({
            icon: 'warning',
            title: '請先選擇搜尋條件！',
            confirmButtonText: '確定'
        })
    } else if(!keyword) {
        Swal.fire({
            icon: 'warning',
            title: '請輸入關鍵字！',
            confirmButtonText: '確定'
        })
    } else if(column.includes('memberID')) {
        if(keyword.length !== 8) {
            console.log("Yeet");
            Swal.fire({
                icon: 'warning',
                title: '會員編號為8碼！',
                confirmButtonText: '確定'
            })
        } else {
            submitForm();
        }
    } else {
        submitForm();
    }
});

function submitForm() {
    const columnInput = document.createElement('input');
    columnInput.type = "hidden";
    columnInput.name = "c";
    columnInput.value = column;
    form.appendChild(columnInput);
    form.submit();
}