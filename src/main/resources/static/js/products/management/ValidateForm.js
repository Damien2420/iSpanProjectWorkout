function validateForm() {
    let isValid = true;
    $('.validate').each(function () {
        if ($(this).val().trim() === '') {
            isValid = false;
            return false;
        }
    });
    return isValid;
}