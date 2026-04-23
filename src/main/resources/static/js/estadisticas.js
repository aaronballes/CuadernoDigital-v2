console.log('estadisticas.js loaded');
let datosCompletos = [];
let propietarios = [];
let charts = [];
let datosActuales = [];

function cargarDatos() {
    console.log('cargarDatos called');
    Promise.all([
        fetch('/api/estadisticas/superficie-por-cultivo').then(r => r.json()),
        fetch('/api/propietarios').then(r => r.json())
    ]).then(([datos, props]) => {
        console.log('Data loaded:', datos.length, 'records');
        datosCompletos = datos;
        propietarios = props;
        console.log('propietarios:', props);
        propietarios = props;
        
        const ejer = [...new Set(datos.map(d => d.ejercicio))];
        const filtroEjer = document.getElementById('filtro-ejercicio');
        filtroEjer.innerHTML = '<option value="">Todos</option>' +
            ejer.map(e => '<option value="' + e + '">' + e + '</option>').join('');
        
        const filtroProp = document.getElementById('filtro-propietario');
        const propsUnicos = [...new Set(datos.map(d => d.propietario))];
        filtroProp.innerHTML = '<option value="">Todos</option>' +
            propsUnicos.map(p => '<option value="' + p + '">' + p + '</option>').join('');
        
        filtrarYMostrar();
    }).catch(err => {
        document.getElementById('content-tabla').innerHTML = '<div class="empty">Error al cargar datos: ' + err + '</div>';
    });
}

function cambiarVista(vista) {
    document.querySelectorAll('.vista-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    if (vista === 'tabla') {
        document.getElementById('vista-tabla').classList.remove('oculto');
        document.getElementById('vista-grafico').classList.add('oculto');
    } else {
        document.getElementById('vista-tabla').classList.add('oculto');
        document.getElementById('vista-grafico').classList.remove('oculto');
        generarGraficos();
    }
}

function filtrarYMostrar() {
    const ejercicioId = document.getElementById('filtro-ejercicio').value;
    const propietarioId = document.getElementById('filtro-propietario').value;
    
    datosActuales = datosCompletos;
    if (ejercicioId) datosActuales = datosActuales.filter(d => d.ejercicio === ejercicioId);
    if (propietarioId) datosActuales = datosActuales.filter(d => d.propietario === propietarioId);
    
    if (datosActuales.length === 0) {
        document.getElementById('content-tabla').innerHTML = '<div class="empty">No hay datos disponibles</div>';
        return;
    }
    
    const propietarioAgrupado = {};
    datosActuales.forEach(d => {
        const propKey = d.propietario || 'Sin propietario';
        if (!propietarioAgrupado[propKey]) {
            propietarioAgrupado[propKey] = {};
        }
        if (!propietarioAgrupado[propKey][d.ejercicio]) {
            propietarioAgrupado[propKey][d.ejercicio] = {
                nombre: d.ejercicio,
                total: d.total,
                cultivos: []
            };
        }
        propietarioAgrupado[propKey][d.ejercicio].cultivos.push(d);
    });
    
    let html = '';
    for (const [propNombre, ejerciciosData] of Object.entries(propietarioAgrupado)) {
        html += '<h2 style="margin-top: 2rem; color: var(--primary); border-bottom: 2px solid var(--primary); padding-bottom: 0.5rem;">👤 ' + propNombre + '</h2>';
        
        for (const [ejerNombre, ejerData] of Object.entries(ejerciciosData)) {
            html += '<h3 style="margin-top: 1.5rem; margin-left: 1rem;">📅 Ejercicio: ' + ejerNombre + ' (Total: ' + ejerData.total.toFixed(2) + ' ha)</h3>';
            html += '<div class="table-container" style="margin-left: 1rem;"><table><thead><tr><th>Cultivo</th><th>Superficie (ha)</th><th>%</th><th>Distribución</th></tr></thead><tbody>';
            
            ejerData.cultivos.forEach(function(c) {
                html += '<tr>';
                html += '<td><strong>' + c.cultivo + '</strong></td>';
                html += '<td>' + c.superficie.toFixed(2) + '</td>';
                html += '<td>' + c.porcentaje + '%</td>';
                html += '<td><div class="porcentaje-bar" style="width: ' + Math.min(c.porcentaje, 100) + '%"><span class="porcentaje-text">' + c.porcentaje + '%</span></div></td>';
                html += '</tr>';
            });
            
            html += '<tr class="total-row"><td><strong>Total</strong></td><td>' + ejerData.total.toFixed(2) + '</td><td>100%</td><td></td></tr>';
            html += '</tbody></table></div>';
        }
    }
    
    document.getElementById('content-tabla').innerHTML = html;
}

function generarGraficos() {
    const container = document.getElementById('charts-container');
    container.innerHTML = '';
    
    charts.forEach(c => c.destroy());
    charts = [];
    
    if (datosActuales.length === 0) {
        container.innerHTML = '<div class="empty">No hay datos para mostrar gráficos</div>';
        return;
    }
    
    const ejercicios = [...new Set(datosActuales.map(d => d.ejercicio))];
    const propKeys = [...new Set(datosActuales.map(d => d.propietario || 'Sin propietario'))];
    
    const colores = [
        '#2d5a27', '#4a8f47', '#8bc34a', '#ffc107', '#ff9800', 
        '#f44336', '#e91e63', '#9c27b0', '#673ab7', '#2196f3'
    ];
    
    propKeys.forEach(function(prop, idxProp) {
        const propData = datosActuales.filter(d => (d.propietario || 'Sin propietario') === prop);
        
        ejercicios.forEach(function(ejer, idxEjer) {
            const ejerData = propData.filter(d => d.ejercicio === ejer);
            if (ejerData.length === 0) return;
            
            const card = document.createElement('div');
            card.className = 'chart-card';
            
            const canvas = document.createElement('canvas');
            const chartDiv = document.createElement('div');
            chartDiv.className = 'chart-container';
            chartDiv.appendChild(canvas);
            
            const titulo = document.createElement('h3');
            titulo.textContent = prop + ' - ' + ejer;
            
            card.appendChild(titulo);
            card.appendChild(chartDiv);
            container.appendChild(card);
            
            const labels = ejerData.map(d => d.cultivo);
            const values = ejerData.map(d => d.superficie);
            const backgroundColors = ejerData.map(function(_, i) {
                return colores[i % colores.length];
            });
            
            const chart = new Chart(canvas, {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        data: values,
                        backgroundColor: backgroundColors,
                        borderWidth: 2,
                        borderColor: '#fff'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });
            
            charts.push(chart);
        });
    });
}

function exportarExcel() {
    if (datosActuales.length === 0) {
        alert('No hay datos para exportar');
        return;
    }
    
    const ejercicio = document.getElementById('filtro-ejercicio').value || 'Todos';
    const propietario = document.getElementById('filtro-propietario').value || 'Todos';
    
    const datosOrdenados = datosActuales.map(d => ({
        'Propietario': d.propietario || 'Sin propietario',
        'Ejercicio': d.ejercicio,
        'Cultivo': d.cultivo,
        'Superficie (ha)': d.superficie,
        'Porcentaje (%)': d.porcentaje,
        'Total Ejercicio (ha)': d.total
    }));
    
    const ws = XLSX.utils.json_to_sheet(datosOrdenados);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Estadísticas');
    
    const colWidths = [
        {wch: 20}, {wch: 15}, {wch: 20}, {wch: 15}, {wch: 15}, {wch: 20}
    ];
    ws['!cols'] = colWidths;
    
    const filename = 'Estadisticas_' + ejercicio + '_' + propietario + '.xlsx';
    XLSX.writeFile(wb, filename);
}

function exportarPDF() {
    if (datosActuales.length === 0) {
        alert('No hay datos para exportar');
        return;
    }
    
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    
    const ejercicio = document.getElementById('filtro-ejercicio').value || 'Todos';
    const propietario = document.getElementById('filtro-propietario').value || 'Todos';
    
    doc.setFontSize(18);
    doc.setTextColor(45, 90, 39);
    doc.text('Estadísticas de Superficie por Cultivo', 14, 20);
    
    doc.setFontSize(10);
    doc.setTextColor(100);
    doc.text('Filtros - Ejercicio: ' + ejercicio + ' | Propietario: ' + propietario, 14, 28);
    
    const propietarioAgrupado = {};
    datosActuales.forEach(d => {
        const propKey = d.propietario || 'Sin propietario';
        if (!propietarioAgrupado[propKey]) {
            propietarioAgrupado[propKey] = {};
        }
        if (!propietarioAgrupado[propKey][d.ejercicio]) {
            propietarioAgrupado[propKey][d.ejercicio] = {
                nombre: d.ejercicio,
                total: d.total,
                cultivos: []
            };
        }
        propietarioAgrupado[propKey][d.ejercicio].cultivos.push(d);
    });
    
    let yPos = 35;
    
    for (const [propNombre, ejerciciosData] of Object.entries(propietarioAgrupado)) {
        if (yPos > 250) {
            doc.addPage();
            yPos = 20;
        }
        
        doc.setFontSize(12);
        doc.setTextColor(45, 90, 39);
        doc.text('Propietario: ' + propNombre, 14, yPos);
        yPos += 7;
        
        for (const [ejerNombre, ejerData] of Object.entries(ejerciciosData)) {
            if (yPos > 250) {
                doc.addPage();
                yPos = 20;
            }
            
            doc.setFontSize(10);
            doc.setTextColor(0);
            doc.text('Ejercicio: ' + ejerNombre + ' (Total: ' + ejerData.total.toFixed(2) + ' ha)', 14, yPos);
            yPos += 5;
            
            const tableData = ejerData.cultivos.map(c => [
                c.cultivo,
                c.superficie.toFixed(2) + ' ha',
                c.porcentaje + '%'
            ]);
            
            doc.autoTable({
                startY: yPos,
                head: [['Cultivo', 'Superficie', '%']],
                body: tableData,
                theme: 'striped',
                headStyles: { fillColor: [45, 90, 39] },
                margin: { left: 14 },
                width: 180
            });
            
            yPos = doc.lastAutoTable.finalY + 10;
        }
        
        yPos += 5;
    }
    
    const filename = 'Estadisticas_' + ejercicio + '_' + propietario + '.pdf';
    doc.save(filename);
}

cargarDatos();
