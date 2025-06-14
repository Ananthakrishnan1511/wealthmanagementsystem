document.addEventListener('DOMContentLoaded', () => {
    const darkModeToggle = document.getElementById('darkModeToggle');
    const body = document.body;
    const currentTheme = localStorage.getItem('theme');

    // Apply saved theme on page load
    if (currentTheme) {
        body.setAttribute('data-theme', currentTheme);
        // Update icon based on theme
        if (currentTheme === 'dark') {
            darkModeToggle.querySelector('i').classList.remove('fa-moon');
            darkModeToggle.querySelector('i').classList.add('fa-sun');
        } else {
            darkModeToggle.querySelector('i').classList.remove('fa-sun');
            darkModeToggle.querySelector('i').classList.add('fa-moon');
        }
    }

    darkModeToggle.addEventListener('click', () => {
        if (body.getAttribute('data-theme') === 'dark') {
            body.setAttribute('data-theme', 'light');
            localStorage.setItem('theme', 'light');
            darkModeToggle.querySelector('i').classList.remove('fa-sun');
            darkModeToggle.querySelector('i').classList.add('fa-moon');
        } else {
            body.setAttribute('data-theme', 'dark');
            localStorage.setItem('theme', 'dark');
            darkModeToggle.querySelector('i').classList.remove('fa-moon');
            darkModeToggle.querySelector('i').classList.add('fa-sun');
        }
    });
});