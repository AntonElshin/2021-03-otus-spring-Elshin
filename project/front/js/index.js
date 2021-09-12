async function sendFile() {
    const formData = new FormData();
    const fileField = document.querySelector('input[type="file"]');

    formData.append('file', fileField.files[0]);

    const httpHeaders = {
        'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiJ9._BBLSLFVxDRW7Oeg0s01_Vj-NOOp1YoTMDyGDAIHnHl3aBVZkZ32dxSyY2DClHpSgwIdHHSta-gIaMbYkqMBBA'
    };

    try {
        const response = await fetch('http://localhost:8080/file-upload/vehiclemodels/passenger-used', {
            headers: new Headers(httpHeaders),
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        console.log('Успех:', result);
    } catch (error) {
        console.error('Ошибка:', error);
    }
}