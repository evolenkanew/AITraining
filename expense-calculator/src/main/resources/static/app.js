let expenses = [];

function addRow(category = '', amount = '') {
    const tbody = document.getElementById('expensesBody');
    const row = document.createElement('tr');
    row.innerHTML = `
        <td><input type="text" value="${category}" placeholder="Category"></td>
        <td><input type="number" value="${amount}" placeholder="Amount" min="0"></td>
        <td><button onclick="removeRow(this)">Remove</button></td>
    `;
    tbody.appendChild(row);
}

function removeRow(btn) {
    btn.closest('tr').remove();
}

function getExpensesFromTable() {
    const rows = document.querySelectorAll('#expensesBody tr');
    const data = [];
    rows.forEach(row => {
        const category = row.children[0].querySelector('input').value.trim();
        const amount = parseFloat(row.children[1].querySelector('input').value);
        if (category && !isNaN(amount)) {
            data.push({ category, amount });
        }
    });
    return data;
}

function clearAll() {
    fetch('/api/expenses', { method: 'DELETE' })
        .then(() => {
            document.getElementById('expensesBody').innerHTML = '';
            document.getElementById('results').innerHTML = '';
        });
}

function calculate() {
    const data = getExpensesFromTable();
    // Clear backend and re-add all current expenses
    fetch('/api/expenses', { method: 'DELETE' })
        .then(() => Promise.all(data.map(exp =>
            fetch('/api/expenses', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(exp)
            })
        )))
        .then(() => fetch('/api/expenses/summary'))
        .then(res => res.json())
        .then(showResults);
}

function showResults(summary) {
    const resultsDiv = document.getElementById('results');
    resultsDiv.innerHTML = `
        <h2>Results</h2>
        <p><strong>Total amount of expenses:</strong> $${summary.total.toLocaleString()}</p>
        <p><strong>Average daily expense:</strong> $${summary.averageDaily.toLocaleString(undefined, { maximumFractionDigits: 2 })}</p>
        <p><strong>Top 3 expenses:</strong></p>
        <ol>
            ${summary.top3.map(e => `<li>${e.category} ($${e.amount.toLocaleString()})</li>`).join('')}
        </ol>
    `;
}

// Add initial rows for demo
addRow('Groceries', 15000);
addRow('Rent', 40000);
addRow('Transportation', 5000);
addRow('Entertainment', 10000);
addRow('Communication', 2000);
addRow('Gym', 3000); 