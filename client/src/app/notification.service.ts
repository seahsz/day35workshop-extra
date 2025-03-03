import { inject, Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable()
export class NotificationService {

    private _snackBar = inject(MatSnackBar);

    showError(message: string) {
        this._snackBar.open(message, 'Close', {
            duration: 5000, // 5 seconds
            verticalPosition: 'top',
            panelClass: ['error-snackbar']
        })
    }

}