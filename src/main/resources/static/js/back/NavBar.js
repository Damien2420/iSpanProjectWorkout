const path = window.location.pathname;

if (!path.includes('/admin/index')) {
    document.querySelector('#index').classList.remove('active');
}

switch (true) {
    case path.includes('member'):
        toggleNavBarOptions('member-ul', 'a[href*="/getallmembers.controller"]');
        break;
    case path.includes('ChatList'):
        toggleNavBarOptions('member-ul', 'a[href*="/adminChatList"]');
        break;
    case path.includes('coach'):
        toggleNavBarOptions('coach-ul', 'a[href*="/coach/CoachGetAll"]');
        break;
    case path.includes('course'):
        toggleNavBarOptions('coach-ul', 'a[href*="/course/CourseGetAll"]');
        break;
    case path.includes('schedule'):
        toggleNavBarOptions('coach-ul', 'a[href*="/schedule/ScheduleGetAll"]');
        break;
    case path.includes('products'):
        toggleNavBarOptions('mall-ul', 'a[href*="/products/allList"]');
        break;
    case path.includes('Menu'):
		toggleNavBarOptions('menu-ul', 'a[href*="/foodMenu/allMenus"]');
		break;
    case path.includes('food'):
        toggleNavBarOptions('menu-ul', 'a[href*="/food/ShowFood"]');
        break;
    case path.includes('Reports'):
        toggleNavBarOptions('fourm-ul', 'a[href*="/forum/getAllReports"]');
        break;
    case path.includes('Comments'):
        toggleNavBarOptions('fourm-ul', 'a[href*="/forum/getAllComments"]');
        break;
    case path.includes('Articles'):
        toggleNavBarOptions('fourm-ul', 'a[href*="/forum/getAllArticles"]');
        break;
    case path.includes('ticket/list'):
        toggleNavBarOptions('ticket-ul', 'a[href*="/ticket/list"]');
        break;
    case path.includes('ticketorder'):
        toggleNavBarOptions('ticket-ul', 'a[href*="/ticketorder/selectall"]');
        break;
    case path.includes('ticket/add'):
        toggleNavBarOptions('ticket-ul', 'a[href*="/ticket/add"]');
        break;
    case path.includes('order'):
        toggleNavBarOptions('mall-ul', 'a[href*="/order/all"]');
        break;
    default:
        break;
}

function toggleNavBarOptions(ul, li) {
    const ulElement = document.querySelector('#' + ul);
    ulElement.classList.add('open');
    const selectedli = document.querySelector('#' + ul + ' ' + li).parentElement;
    selectedli.classList.add('active');
}