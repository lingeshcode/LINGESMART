# Windows Native PowerShell Web Server for LingeshMart (Zero Dependencies Required)
$port = 8080
$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add("http://localhost:$port/")
$listener.Start()

Write-Host "===================================================" -ForegroundColor Green
Write-Host "  LingeshMart is LIVE at: http://localhost:$port/lingeshmart" -ForegroundColor Yellow
Write-Host "===================================================" -ForegroundColor Green

$htmlFile = Join-Path $PSScriptRoot "LingeshMart.html"

while ($listener.IsListening) {
    $context = $listener.GetContext()
    $request = $context.Request
    $response = $context.Response

    $content = Get-Content -Path $htmlFile -Raw -Encoding UTF8
    $buffer = [System.Text.Encoding]::UTF8.GetBytes($content)
    
    $response.ContentLength64 = $buffer.Length
    $response.ContentType = "text/html; charset=utf-8"
    $output = $response.OutputStream
    $output.Write($buffer, 0, $buffer.Length)
    $output.Close()
}
