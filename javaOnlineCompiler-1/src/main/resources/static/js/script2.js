document.getElementById('run').addEventListener('click', () => {
    const code = document.getElementById('editor').value;
    const consoleElement = document.getElementById('console');
    try {
        const result = eval(code); // Note: Using eval for JavaScript, you will need a Java compiler for Java code
        consoleElement.textContent = result;
    } catch (error) {
        consoleElement.textContent = error;
    }
});

let time = 126;
const timerElement = document.getElementById('timer');

const countdown = setInterval(() => {
    time--;
    timerElement.textContent = time + "秒";
    if (time <= 0) {
        clearInterval(countdown);
        // Add code here to transition to the next page or phase
    }
}, 1000);

document.getElementById('finish').addEventListener('click', () => {
    clearInterval(countdown);
    // Add code here to finish the current phase and move to the next
});

async function runCode() {
    const code = document.getElementById('code').value;
    try {
        const response = await fetch('/run', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ code })
        });
        const result = await response.json();
        document.getElementById('console').textContent = result;
    } catch (error) {
        document.getElementById('console').textContent = error.message;
    }
}
