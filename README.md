# Group File Template (GFT) IDEA Plugin

[![Version](https://img.shields.io/badge/Version-4.6-blue.svg)](https://github.com/Louco11/ArchitecturalTemplates/wiki/Release-Notes)
[![Version](https://img.shields.io/badge/IDEA-Marketplace-blue.svg)](https://plugins.jetbrains.com/plugin/16836-architectural-templates)
[![License](https://img.shields.io/github/license/srs/gradle-node-plugin.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![buymeacoffee](https://img.shields.io/badge/buy%20me%20a-coffee-blue)](https://www.buymeacoffee.com/doronec)

Plugin create a group of files by custom templates from IDEA interface.

[Version 4.6](https://github.com/Louco11/Group-File-Template-GFT/wiki/Release-Notes)

# Quick start
[Templates Example](https://github.com/Louco11/Group-File-Template-GFT/tree/master/templates/TestTemplate)

First of all install the [Plugin From Marketplace](https://plugins.jetbrains.com/plugin/16836-architectural-templates).

## Short Template
To create an empty short template, click on the `"Tools"` -> `"GFT Creator"` -> `"Create New Short Template"` menu item

<img src="screencut/create_empty_short_tamplate.png" alt="drawing" width="50%" />

### Add a piece of code to short templates

- Highlight code
- Right-click on the highlight code
- Choose `"Add in Template"`

<img src="screencut/add_pease_of_code.png" alt="drawing" width="50%" />

- If there is more than one template, choose which one to save it to, otherwise the plugin will add it to the only created template
- Enter the name of the short template

<img src="screencut/enter_name.png" alt="drawing" width="50%" />

### Use short templates

Right-click or generic menu

<img src="screencut/righr_click_menu.png" alt="drawing" width="50%" />
<img src="screencut/generic_menu.png" alt="drawing" width="50%" />


### Structure short templates

The heart of the short template is the Json file main_short

| Key            | 	Value                      |	Comment                 |
|----------------|-----------------------------|--------------------------|
| name           | 	Name Short Template        |                          |
| description    | 	Description Short Template |                          |
| path           | 	Path to short Template     |                          |
| addFile        | 	Files to create            |	list object File        |

structure `addFile`

| Key        | Value	                              | comment |
|------------|-------------------------------------|---------|
| name       | name short template in menu         |         |
| filePath   | file with template                	 |         |


## Template
To create an empty template, click on the `"Tools"` -> `"GFT Creator"`->`"Create New Template"` menu item

<img src="screencut/create_empty_template.png" alt="drawing" width="30%" />

In the Dialog box enter the name of the template

<img src="screencut/Create%20File%20from%20Template.png" alt="drawing" width="50%" />

The plugin will create an empty template at the root of your project.

All templates are stored in the folder `"Your project name"/templates`

<img src="screencut/Template%20In%20Tree%20Project.png" alt="drawing" width="20%" />

The heart of the template is the Json file Main



Inside it has a structure

| Key            |	Value                                                |	Comment                 |
|----------------|-------------------------------------------------------|--------------------------|
| name           |	Name Template                                        |                          |
| description    |	Description Template                                 |                          |
| path           |	Path to Template                                     |                          |
| param          |	Variables to insert into the template                |	list String             |
| selectParam    |	Variables to drop list with values into the template |	list object SelectParam |
| addFile        |	Files to create                                      |	list object File        |

structure `SelectParam`

| Key           | Value	                                | comment       |
|---------------|---------------------------------------|---------------|
| paramName     | Variables to insert into the template |               |
| paramValue    | values                	            | list String   |

### param

The parameter is an array of strings. It can be in the File Structure in name and path. 
And also in the template itself. It is declared in brackets `{param}`.

Options after param:
* `[-S]` - SCREAMING_SNAKE_CASE
* `[-s]` - snake_case
* `[-C]` - CamelCase
* `[-c]` - camelCase
* `[-p]` - point.between.words
* `[-sl]` - slash/between/word
* `[-d]` - dash-between-word

example {"NewFeature"}[-s] equals new_feature

Example

<img src="screencut/ParamExample.png" alt="drawing" width="40%" />
<img src="screencut/ParamExample2.png" alt="drawing" width="50%" />

Default parameter for Java and Kotlin `{package}` and `{pack}` for R.class example `import {pack}.R`

`{time}` = 10:56

`{day}` = 04

`{month}` = 06

`{year}` = 2022

When creating files from a template, the plugin will correct to fill in the parameter fields.

<img src="screencut/FillParam.png" alt="drawing" width="50%" />
<img src="screencut/FillParam2.png" alt="drawing" width="30%" />

### File it has a structure

| Key               |	Value                                                   |	Comment                                                                                                  |
|-------------------|-----------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| name              |	Name when creating a file                               |	You can use Param in the name                                                                            |
| path              |	Additional directories for saving                       |	You can use Param in the name. Creates a catalog automatically if it does not exist                      |
| fileTemplatePath  |	The name of the template from which the file is created |	It must be specified with the extension .tm and you can specify the directory where this file is located |

If file name empty then create only directory

To add resources to android, write the `"res/"` to the parameter `"path"` parameter

To add test to android, write the `"test/"` to the parameter `"path"` parameter

To add file in path project, write the `"~/"` to the parameter `"path"` parameter

# Create Template

To create a file from a template, right-click on the path in which 
we want to create and select the template we need from the list

<img src="screencut/Create%20File%20from%20Template.png" alt="drawing" width="50%" />

### Add File In Template

To add a file to the template, right-click on it and select `"Add file in template"` 
The plugin will ask you to choose which template you want to add (if there are several of them) 
and will ask you to rename the file as it will be named in the template.

<img src="screencut/addFile1.png" alt="drawing" width="50%" />
<img src="screencut/addFile2.png" alt="drawing" width="30%" />
<img src="screencut/addFile3.png" alt="drawing" width="50%" />
<img src="screencut/addFile4.png" alt="drawing" width="40%" />
<img src="screencut/addFile5.png" alt="drawing" width="60%" />

# License

```
Copyright 2022 Doroncov Mihail

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
