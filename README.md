# Simple Antivirus Scanner

A lightweight antivirus-style scanner made in Java that scans a **selected folder** for suspicious files, isolates them into a **quarantine folder**, and generates a **log report** explaining why each file was flagged.

## Features

### Folder Scanner
- Scans only the folder chosen by the user.
- Fast and focused scanning without affecting the whole system.

### Suspicious File Detection
Includes a built-in detector that flags files based on:

- Unusual or suspicious file extensions
- File names commonly associated with malware
- Potentially dangerous patterns

### Custom Hash Database
The scanner also supports a user-editable JSON database.

- Add any file hashes you want to monitor
- If a scanned file matches a stored hash, it will be quarantined automatically

### Quarantine System
- Suspicious files are moved into a dedicated `quarantine` folder
- Keeps the scanned directory cleaner and safer

### Detailed Logs
After every scan, a log file is created containing:

- File name
- Detection reason
- Detection type (rule-based / hash match)
- Scan results

## How It Works

1. Launch the application  
2. Select a folder to scan  
3. The scanner checks all files inside it  
4. Suspicious files are moved to `quarantine/`  
5. A scan log is generated  

## Hash Database

The custom hash database starts empty.

You can manually add hashes to the JSON file to detect:

- Known malware samples
- Dangerous executables
- Test files
- Any file you want blocked

## Technologies Used

- Java
- JSON storage
- File system operations
- Hash comparison

## Disclaimer

This project is made for **educational purposes** and is **not a replacement for professional antivirus software** such as Windows Defender, Malwarebytes, Bitdefender, etc.

## Future Ideas

- Real-time protection
- Recursive scanning
- GUI improvements
- Automatic hash updates
- File restore from quarantine
- Better heuristic analysis

## License

Open-source project for learning and experimentation.
