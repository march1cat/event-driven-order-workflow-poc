-- 1. 零件 A 系列 - 庫存充足 (完全沒有人預定)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-A001-2026', 500, 0);

-- 2. 零件 A 系列 - 已消耗/預定部分 (總數 500，已被預定 80)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-A002-2026', 500, 0);

-- 3. 零件 B 系列 - 低庫存報警 (總數 100，已經預定 95，剩餘可用 5)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-B105-X', 100, 0);

-- 4. 零件 B 系列 - 已售罄 (總數 100，100 個全被預定)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-B201-X', 100, 0);

-- 5. 零件 C 系列 - 高單價少量存貨 (入庫 10，無人預定)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-C999-PREMIUM', 10, 0);

-- 6. 零件 C 系列 - 高單價被預定一單位 (入庫 10，鎖定 1)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-C998-PREMIUM', 10, 0);

-- 7. 零件 D 系列 - 大量批發規格 (總數 2000，預定中 150)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-D-BULK-01', 2000, 0);

-- 8. 零件 D 系列 - 大量批發規格 (新進貨，無人預定)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'PN-D-BULK-02', 5000, 0);

-- 9. 測試用零件 - 剩餘一半可動用 (總數 60，預定 30)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'TEST-PART-5050', 60, 0);

-- 10. 舊型號零件 - 庫存出清中 (總數 200，預定 188)
INSERT INTO ProductItemLeftQuantity (Id, PartNo, Quantity, FreezeQuantity) VALUES (UUID(), 'OLD-MODEL-2024', 200, 0);