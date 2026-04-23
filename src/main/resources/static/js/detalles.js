console.log('detalles.js loaded');
const API = '/api/detalles';
const API_PROPIETARIOS = '/api/propietarios';
let ejercicios = [];
let parcelasMap = new Map();
let cultivos = [];
let propietarios = [];
let datosCompletos = [];
let filtroActual = null;
let filtroPropietario = null;
let alertasActivas = new Set();
let modalData = { parcelaId: null, ejercicioId: null, ejercicioNombre: null, cultivoActual: null };

async function init() {
    console.log('init called');
    try {
        const [ejerciciosRes, cultivosRes, propietariosRes] = await Promise.all([
            fetch('/api/ejercicios').then(r => r.json()),
            fetch('/api/cultivos').then(r => r.json()),
            fetch(API_PROPIETARIOS).then(r => r.json())
        ]);
        
        console.log('API responses - ejercicios:', ejerciciosRes?.length, 'cultivos:', cultivosRes?.length, 'propietarios:', propietariosRes?.length);
        
        ejercicios = ejerciciosRes;
        cultivos = cultivosRes;
        propietarios = propietariosRes;
        
        document.getElementById('filtro-propietario').innerHTML = 
            '<option value="">Todos</option>' +
            propietarios.map(p => `<option value="${p.propietarioId}">${p.propietario}</option>`).join('');
        
        document.getElementById('filtro-ejercicio').innerHTML = 
            '<option value="">Todos los ejercicios</option>' +
            ejercicios.map(e => `<option value="${e.ejercicioId}">${e.ejercicio || e.ejercicioId}</option>`).join('');
        
        await cargarDatos();
    } catch (err) {
        console.error('Error in init:', err);
    }
}

async function cargarDatos() {
    console.log('cargarDatos called, filtroActual:', filtroActual);
    document.getElementById('loading').style.display = 'block';
    document.getElementById('table-container').style.display = 'none';
    
    let url = API;
    if (filtroActual) url += '?ejercicio=' + filtroActual;
    
    console.log('Fetching from:', url);
    
    try {
        const response = await fetch(url);
        console.log('Response status:', response.status);
        datosCompletos = await response.json();
        console.log('Data received:', datosCompletos.length, 'records');
        aplicarFiltros();
    } catch (err) {
        console.error('Error loading data:', err);
        alert('Error al cargar datos: ' + err);
    }
    
    document.getElementById('loading').style.display = 'none';
    document.getElementById('table-container').style.display = 'block';
}

function aplicarFiltros() {
    let datosFiltrados = datosCompletos;
    if (filtroPropietario) {
        datosFiltrados = datosFiltrados.filter(d => d.propietarioId == filtroPropietario);
    }
    generarTabla(datosFiltrados);
}

function generarTabla(datos) {
    const ejerciciosMostrar = filtroActual 
        ? ejercicios.filter(e => String(e.ejercicioId) === String(filtroActual))
        : ejercicios;
    
    const conCultivo = datos.filter(d => d.cultivoId > 0).length;
    document.getElementById('stat-total').textContent = datos.length;
    document.getElementById('stat-con').textContent = conCultivo;
    document.getElementById('stat-sin').textContent = datos.length - conCultivo;
    document.getElementById('stat-alertas').textContent = alertasActivas.size;
    
    let headerHtml = '<tr><th>Parcela</th><th>Propietario</th><th>Polígono</th><th>Nº</th><th>Sup.</th>';
    ejerciciosMostrar.forEach(e => {
        headerHtml += `<th>${e.ejercicio || e.ejercicioId}</th>`;
    });
    headerHtml += '</tr>';
    document.getElementById('table-head').innerHTML = headerHtml;
    
    parcelasMap = new Map();
    alertasActivas = new Set();
    
    datos.forEach(d => {
        if (!parcelasMap.has(d.parcelaId)) {
            parcelasMap.set(d.parcelaId, {
                nombre: d.nombre,
                poligono: d.poligono,
                parcela: d.parcela,
                superficie: d.superficie,
                propietario: d.propietario || '-',
                ejercicios: {},
                alerta: d.alertaRepeticion
            });
        }
        parcelasMap.get(d.parcelaId).ejercicios[d.ejercicioId] = {
            cultivoId: d.cultivoId,
            cultivo: d.cultivo
        };
        if (d.alertaRepeticion) alertasActivas.add(d.parcelaId);
    });
    
    document.getElementById('stat-alertas').textContent = alertasActivas.size;
    
    let bodyHtml = '';
    parcelasMap.forEach((p, parcelaId) => {
        const esAlerta = alertasActivas.has(parcelaId);
        bodyHtml += `<tr class="${esAlerta ? 'row-alerta' : ''}">
            <td class="parcela-info">${p.nombre || '-'}${esAlerta ? ' ⚠️' : ''}</td>
            <td class="parcela-info">${p.propietario}</td>
            <td class="parcela-info">${p.poligono || '-'}</td>
            <td class="parcela-info">${p.parcela || '-'}</td>
            <td class="parcela-info">${p.superficie || '-'}</td>`;
        
        ejerciciosMostrar.forEach(e => {
            const dato = p.ejercicios[e.ejercicioId] || { cultivoId: 0, cultivo: '' };
            const tieneCultivo = dato.cultivoId > 0;
            const esCeldaAlerta = esAlerta && tieneCultivo;
            
            let badge = tieneCultivo 
                ? (esCeldaAlerta 
                    ? `<span class="badge badge-danger">${dato.cultivo}</span>` 
                    : `<span class="badge badge-success">${dato.cultivo}</span>`)
                : '<span class="badge badge-warning">-</span>';
            
            bodyHtml += `<td class="cultivo-cell">
                ${badge}
                <button class="btn-sm btn-change" style="margin-top:0.5rem;" 
                    onclick="abrirModal(${parcelaId}, '${e.ejercicioId}', '${e.ejercicio || e.ejercicioId}', ${dato.cultivoId})">
                    ${tieneCultivo ? 'Cambiar' : 'Asignar'}
                </button>
            </td>`;
        });
        
        bodyHtml += '</tr>';
    });
    
    document.getElementById('table-body').innerHTML = bodyHtml;
}

function filtrar() {
    filtroActual = document.getElementById('filtro-ejercicio').value;
    filtroPropietario = document.getElementById('filtro-propietario').value;
    aplicarFiltros();
}

function resetearFiltro() {
    filtroActual = null;
    filtroPropietario = null;
    document.getElementById('filtro-ejercicio').value = '';
    document.getElementById('filtro-propietario').value = '';
    aplicarFiltros();
}

function abrirModal(parcelaId, ejercicioId, ejercicioNombre, cultivoActual) {
    modalData = { parcelaId, ejercicioId, ejercicioNombre, cultivoActual };
    
    const parcela = parcelasMap.get(parcelaId);
    document.getElementById('modal-info').textContent = 
        `Parcela: ${parcela.nombre || '-'} | Ejercicio: ${ejercicioNombre}`;
    
    document.getElementById('modal-alerta').classList.remove('visible');
    
    document.getElementById('modal-cultivo').innerHTML = 
        '<option value="0">-- Sin cultivo --</option>' +
        cultivos.map(c => `<option value="${c.cultivoId}" ${c.cultivoId == cultivoActual ? 'selected' : ''}>${c.cultivo}</option>`).join('');
    
    document.getElementById('modal').classList.add('active');
}

function verificarCambio() {
    const nuevoId = parseInt(document.getElementById('modal-cultivo').value);
    const modalAlerta = document.getElementById('modal-alerta');
    
    if (!nuevoId || nuevoId === modalData.cultivoActual) {
        modalAlerta.classList.remove('visible');
        return;
    }
    
    const parcela = parcelasMap.get(modalData.parcelaId);
    const cultivosArr = Object.values(parcela.ejercicios).map(e => String(e.cultivoId)).filter(id => id !== '0');
    cultivosArr.push(String(nuevoId));
    
    let repetidos = 0;
    for (let i = 1; i < cultivosArr.length; i++) {
        if (cultivosArr[i] === cultivosArr[i-1]) repetidos++;
        else repetidos = 0;
        if (repetidos >= 2) break;
    }
    
    if (repetidos >= 2) {
        modalAlerta.textContent = '⚠️ ERROR: Se crearán 3+ ejercicios consecutivos con el mismo cultivo';
        modalAlerta.classList.add('visible');
        modalAlerta.style.background = '#f8d7da';
        modalAlerta.style.color = '#721c24';
    } else if (repetidos === 1) {
        modalAlerta.textContent = '⚠️ AVISO: Se crearán 2 ejercicios consecutivos';
        modalAlerta.classList.add('visible');
        modalAlerta.style.background = '#fff3cd';
        modalAlerta.style.color = '#856404';
    } else {
        modalAlerta.classList.remove('visible');
    }
}

function cerrarModal() {
    document.getElementById('modal').classList.remove('active');
}

function exportarExcel() {
    console.log('exportarExcel called');
    
    if (typeof XLSX === 'undefined') {
        alert('Biblioteca Excel no cargada. Recarga la página.');
        return;
    }
    if (datosCompletos.length === 0) {
        alert('No hay datos para exportar');
        return;
    }
    
    let datos = datosCompletos;
    const ejercicioFiltro = document.getElementById('filtro-ejercicio').value || filtroActual;
    const propFiltro = document.getElementById('filtro-propietario').value;
    
    if (filtroActual) {
        datos = datos.filter(d => String(d.ejercicioId) === String(filtroActual));
    }
    if (propFiltro) datos = datos.filter(d => d.propietarioId == propFiltro);
    
    if (datos.length === 0) {
        alert('No hay datos para exportar');
        return;
    }
    
    const datosOrdenados = datos.map(d => ({
        'Parcela': d.nombre,
        'Polígono': d.poligono,
        'Nº Parcela': d.parcela,
        'Superficie': d.superficie,
        'Cultivo': d.cultivo || '-'
    }));
    
    const ws = XLSX.utils.json_to_sheet(datosOrdenados);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Detalles');
    
    const colWidths = [
        {wch: 30}, {wch: 10}, {wch: 10}, {wch: 12}, {wch: 20}
    ];
    ws['!cols'] = colWidths;
    
    const filename = 'Detalles_' + (ejercicioFiltro || 'todos') + '.xlsx';
    XLSX.writeFile(wb, filename);
}

function exportarPDF() {
    console.log('exportarPDF called');
    
    if (typeof window.jspdf === 'undefined') {
        alert('Biblioteca PDF no cargada. Recarga la página.');
        return;
    }
    
    const jsPDF = window.jspdf.jsPDF;
    const doc = new jsPDF();
    
    if (datosCompletos.length === 0) {
        alert('No hay datos para exportar');
        return;
    }
    
    let datos = datosCompletos;
    const propFiltro = document.getElementById('filtro-propietario').value;
    
    if (filtroActual) {
        datos = datos.filter(d => String(d.ejercicioId) === String(filtroActual));
    }
    if (propFiltro) datos = datos.filter(d => d.propietarioId == propFiltro);
    
    if (datos.length === 0) {
        alert('No hay datos para exportar');
        return;
    }
    
    const ejerciciosUnicos = [...new Set(datos.map(d => d.ejercicio || d.ejercicioId))].sort().reverse();
    const propNombre = propFiltro ? (datos[0]?.propietario || '') : '';
    
    ejerciciosUnicos.forEach((ejercicio, index) => {
        if (index > 0) {
            doc.addPage();
        }
        
        const ejerDatos = datos.filter(d => (d.ejercicio || d.ejercicioId) === ejercicio);
        
        doc.setFontSize(14);
        doc.setTextColor(45, 90, 39);
        doc.text('Ejercicio ' + ejercicio, 14, 15);
        
        doc.setFontSize(9);
        doc.setTextColor(100);
        if (propFiltro) {
            doc.text('Propietario: ' + propNombre, 14, 22);
        }
        
        const tableData = ejerDatos.map(d => [
            d.nombre || '-',
            d.poligono || '-',
            d.parcela || '-',
            d.superficie || '-',
            d.cultivo || '-'
        ]);
        
        doc.autoTable({
            startY: propFiltro ? 28 : 22,
            head: [['Parcela', 'Pol', 'Nº', 'Sup', 'Cultivo']],
            body: tableData,
            theme: 'striped',
            headStyles: { 
                fillColor: [45, 90, 39],
                fontSize: 9
            },
            bodyStyles: {
                fontSize: 8
            },
            margin: { left: 14, right: 14 },
            columnStyles: {
                0: { cellWidth: 45 },
                1: { cellWidth: 15, halign: 'center' },
                2: { cellWidth: 15, halign: 'center' },
                3: { cellWidth: 20, halign: 'right' },
                4: { cellWidth: 'auto' }
            }
        });
    });
    
    const ejercicioFiltro = document.getElementById('filtro-ejercicio').value || filtroActual;
    const filename = 'Detalles_' + (ejercicioFiltro || 'todos') + '.pdf';
    doc.save(filename);
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('form-detalle').addEventListener('submit', async function(e) {
        e.preventDefault();
        const cultivoId = parseInt(document.getElementById('modal-cultivo').value);
        
        if (cultivoId === 0) {
            if (modalData.cultivoActual > 0) {
                await fetch(API, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        parcelaId: modalData.parcelaId,
                        ejercicioId: modalData.ejercicioId,
                        cultivoId: modalData.cultivoActual
                    })
                });
            }
        } else {
            const r = await fetch(API, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    parcelaId: modalData.parcelaId,
                    ejercicioId: modalData.ejercicioId,
                    cultivoId: cultivoId
                })
            });
            const result = await r.json();
            if (result.alerta) {
                alert('⚠️ ATENCIÓN: Esta parcela ahora tiene 3 ejercicios consecutivos con el mismo cultivo');
            }
        }
        
        cerrarModal();
        await cargarDatos();
    });
    
    init();
});
