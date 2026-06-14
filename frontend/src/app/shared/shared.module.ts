import { NgModule } from '@angular/core';
import { Time12Pipe } from '../pipes/time12.pipe';

/**
 * SharedModule exports reusable pipes, directives, and components
 * that are used across multiple feature modules.
 */
@NgModule({
  declarations: [Time12Pipe],
  exports: [Time12Pipe]
})
export class SharedModule { }
