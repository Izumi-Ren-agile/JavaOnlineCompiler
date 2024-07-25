async function runCode() {
    const code = document.getElementById('editor').value;
    try {
        const response = await fetch('/run', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ code })
        });
        const result = await response.text(); // JSONではなくテキストとして受け取る
        document.getElementById('console').textContent = result;
    } catch (error) {
        document.getElementById('console').textContent = error.message;
    }
}

document.getElementById('run').addEventListener('click', runCode);

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

