<!DOCTYPE html>
<html lang="de">
<meta charset="utf-8"/>
<head>
    <title>Corona-Anwesenheitsliste - ${teamName?html}</title>
</head>
<style>
    body {
        font-family: arial, sans-serif;
        font-size: x-small;
    }

    h1, p {
        text-align: center;
    }

    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;

        page-break-inside: avoid !important;
        margin: 4px 0 4px 0;
    }

    td, th {
        border: 1px solid #aac1e1;
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(even) {
        background-color: #d8d9e0;
    }

    tr:first-child {
        border-bottom: 5px solid #aac1e1;
    }

    td:first-child {
        font-weight: bold;
    }

    td:nth-child(4),
    td:last-child {
        text-align: center;
    }
</style>
<body>

<h1>Anwesenheitsliste</h1>

<p>${teamName?html}</p>

<p><b>Datum:</b> ${date?html} <b>Zeit:</b> ${startTime?html} - ${endTime?html}</p>

<table>
    <tr>
        <th>Name</th>
        <th>Adresse</th>
        <th>Telefon</th>
        <th>Anw.</th>
        <th>Abw.</th>
    </tr>
    <#list participants as participant>
        <tr>
            <td>${participant.name?html}</td>
            <td>${participant.address?html}</td>
            <td>${participant.phoneNumber?html}</td>
            <td><#if participant.status == 'ATTENDED'>X</#if></td>
            <td><#if participant.status == 'ABSENT'>X</#if></td>
        </tr>
    </#list>

</table>

</body>
</html>
