export interface CustomProduct {
    id?: number;
    productName?: string;
    categoryName?: string;
    brand?: string;
    productDescription?: string;
    unit?: string;
    purchasePrice?: number;
    sellingPrice?: number;
    barcode?: string;
    sku?: string;
    isTaxable?: boolean;
    reOrderLevel?: number;
}
