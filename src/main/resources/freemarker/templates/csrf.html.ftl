<script>
    <#outputformat "JavaScript">
    document.body.addEventListener('htmx:configRequest', (event) => {
        const cookies = document.cookie.split(';').filter((item) => item.trim().startsWith('X-CSRF-TOKEN='));
        if (cookies.length === 1) {
            const cookie = cookies[0].trim();
            event.detail.headers['X-XSRF-TOKEN'] = cookie.substring(cookie.indexOf("=") + 1);
        }
    })
    </#outputformat>
</script>