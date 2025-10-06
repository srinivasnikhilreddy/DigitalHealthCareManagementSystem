import { Component } from '@angular/core';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-root',
    imports: [RouterOutlet, /*RouterLink*/ FormsModule, RouterModule],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent
{
    title = 'hcms_client';
}
