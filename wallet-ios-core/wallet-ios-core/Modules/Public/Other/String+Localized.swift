//
//  String+Localized.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/5.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation

extension String {
    static let publicCurrencyCNYName = "Public.currency.name.cny".bil_localized
    static let publicCurrencyUSDName = "Public.currency.name.usd".bil_localized
    static let publicKeyboardHide = "Public.keyboard.hide".bil_localized
    static let publicNetworkConnectServerFaild = "Public.network.connect.sever.failed".bil_localized
    static let publicWalletFetchError = "Public.wallet.fetch.failed".bil_localized
    static let publicWalletExtKeyError = "Public.wallet.error.extKey".bil_localized
    static let publicWalletIDError = "Public.wallet.error.id".bil_localized
    static let publicWalletDataError = "Public.wallet.error.data".bil_localized
    static let publicWalletGetAddressError = "Public.wallet.error.address".bil_localized
    static let publicWalletNotRefresh = "Public.wallet.error.address.dont.need.refresh".bil_localized
    static let publicWalletNoMoreAddress = "Public.wallet.error.address.nomore".bil_localized
    static let publicWalletGenerateAddressError = "Public.wallet.error.address.generate".bil_localized
    static let publicWalletIndexError = "Public.wallet.error.address.index".bil_localized
    static let publicTransactionUnconfirm = "Public.transaction.status.unconfirm".bil_localized
    static let publicTransactionSuccess = "Public.transaction.status.success".bil_localized
    static let publicTransactionFailure = "Public.transaction.status.failure".bil_localized
    static let publicTransactionConfirmed = "Public.transaction.status.confirmed".bil_localized
    static let publicTransactionNoRemark = "Public.transaction.remark.none".bil_localized
    static let publicAlertActionTitle = "Public.alert.action.title".bil_localized
	
    static let homeHomeSectionShortcut = "HomeC.home.section.shortcut".bil_localized
    static let homeHomeSectionAsset = "HomeC.home.section.asset".bil_localized
    static let homeHomeSectionUnconfirm = "HomeC.home.section.unconfirm".bil_localized
	static let homeHomeSectionWallet = "HomeC.home.section.wallet".bil_localized
	static let homeHomeSectionSubTitleShortcut = "HomeC.home.section.subtitle.shortcut".bil_localized
	static let homeHomeSectionSubTitleAsset = "HomeC.home.section.subtitle.asset".bil_localized
	static let homeHomeSectionSubTitleUnconfirm = "HomeC.home.section.subtitle.unconfirm".bil_localized
	static let homeHomeSectionSubTitleWallet = "HomeC.home.section.subtitle.wallet".bil_localized
	static let homeHomeSectionUnitGe = "HomeC.home.section.subtitle.unit.ge".bil_localized
	static let homeHomePopWalletNew = "HomeC.home.pop.wallet.new".bil_localized
	static let homeHomePopWalletImport = "HomeC.home.pop.wallet.import".bil_localized
	static let homeTxDetailHash = "HomeC.tx.detail.cell.hash".bil_localized
	static let homeTxDetailReceiveAddress = "HomeC.tx.detail.cell.address.receive".bil_localized
	static let homeTxDetailSendAddress = "HomeC.tx.detail.cell.address.send".bil_localized
	static let homeTxDetailConfirm = "HomeC.tx.detail.cell.confirm".bil_localized
	static let homeTxDetailRemark = "HomeC.tx.detail.cell.remark".bil_localized
	static let homeTxDetailDate = "HomeC.tx.detail.cell.date".bil_localized
	static let homeTxDetailNoTx = "HomeC.tx.detail.noTx".bil_localized
    static let homeTxDetailTx = "HomeC.tx.detail.tx".bil_localized
	
	static let receiveAddressTipTitle = "ReceiveC.receive.address.tip.title".bil_localized
	static let receiveAddressTipMessage = "ReceiveC.receive.address.tip.msg".bil_localized
	static let receiveAddressTipRefresh = "ReceiveC.receive.address.tip.refresh".bil_localized
	static let receiveSpecificCheckAmountTipTitle = "ReceiveC.specific.check.amount.tip.title".bil_localized
	static let receiveSpecificCheckAmountTipMessageEmpty = "ReceiveC.specific.check.amount.tip.msg.empty".bil_localized
	static let receiveSpecificCheckAmountTipMessageZero = "ReceiveC.specific.check.amount.tip.msg.zero".bil_localized
	
	static let sendSendAddressInvalid = "SendC.send.address.invalid".bil_localized
	static let sendSendAddressEmpty = "SendC.send.address.empty".bil_localized
	static let sendSendAddressInvalidTitle = "SendC.send.address.invalid.title".bil_localized
	static let sendSendAddressError = "SendC.send.address.error".bil_localized
	static let sendAmountCheckTipTitle = "SendC.amount.check.amount.tip.title".bil_localized
	static let sendAmountCheckTipMessageEmpty = "SendC.amount.check.amount.tip.msg.empty".bil_localized
	static let sendAmountCheckTipMessageZero = "SendC.amount.check.amount.tip.msg.zero".bil_localized
	static let sendAmountCheckTipMessageNotEnough = "SendC.amount.check.amount.tip.msg.notEnough".bil_localized
	static let sendConfirmMinute = "SendC.confirm.minute".bil_localized
	static let sendConfirmHour = "SendC.confirm.hour".bil_localized
	static let sendConfirmDay = "SendC.confirm.day".bil_localized
	static let sendConfirmAverageTime = "SendC.confirm.averageTime".bil_localized
	static let sendConfirmNeedSpend = "SendC.confirm.needSpend".bil_localized
	static let sendConfirmNotEnoughBalace = "SendC.confirm.notEnoughBalace".bil_localized
	static let sendConfirmCannotFindChangeAddress = "SendC.confirm.cannotFindChangeAddress".bil_localized
	static let sendConfirmRemarkTooLong = "SendC.confirm.remarkTooLong".bil_localized
	static let sendConfirmSendFailed = "SendC.confirm.sendFailed".bil_localized
	static let sendConfirmPasswordTitle = "SendC.confirm.passwordTitle".bil_localized
	static let sendConfirmPasswordMessageEmpty = "SendC.confirm.passwordMessageEmpty".bil_localized
	static let sendConfirmPasswordMessageWrong = "SendC.confirm.passwordMessageWrong".bil_localized
	static let sendConfirmCancel = "SendC.confirm.cancel".bil_localized
	static let sendConfirmConfirm = "SendC.confirm.confirm".bil_localized
	static let sendConfirmPasswordPlaceholder = "SendC.confirm.passwordPlaceholder".bil_localized
	static let sendConfirmBuildTxFailed = "SendC.confirm.buildTxFailed".bil_localized
	static let sendConfirmSeedFailed = "SendC.confirm.seedFailed".bil_localized
	static let sendConfirmTryLater = "SendC.confirm.tryLater".bil_localized
	static let sendResultInput = "SendC.result.input".bil_localized
	static let sendResultInputName = "SendC.result.inputName".bil_localized
	static let sendResultInputNamePlaceHolder = "SendC.result.inputNamePlaceholder".bil_localized
	static let sendResultAddSuccess = "SendC.result.addSuccess".bil_localized
}

extension String {
	static let meMe_agreementName = "MeC.agreementName".bil_localized
	static let meMe_meSection_preference = "MeC.meSection_preference".bil_localized
	static let meMe_meSection_contacts = "MeC.meSection_contacts".bil_localized
	static let meMe_meSection_wallet = "MeC.meSection_wallet".bil_localized
	static let meMe_meSection_system = "MeC.meSection_system".bil_localized
	static let meMe_meSection_other = "MeC.meSection_other".bil_localized
	static let meMe_meCell_shortcut = "MeC.meCell_shortcut".bil_localized
	static let meMe_meCell_contactBackup = "MeC.meCell_contactBackup".bil_localized
	static let meMe_meCell_contactRecover = "MeC.meCell_contactRecover".bil_localized
	static let meMe_meCell_system = "MeC.meCell_system".bil_localized
	static let meMe_meCell_aboutUs = "MeC.meCell_aboutUs".bil_localized
	static let meMe_contact_yourKey = "MeC.contact_yourKey".bil_localized
	static let meMe_contact_copyKey = "MeC.contact_copyKey".bil_localized
	static let meMe_contact_keyCopied = "MeC.contact_keyCopied".bil_localized
	static let meMe_contact_authFailed = "MeC.contact_authFailed".bil_localized
	static let meMe_contact_inputKey = "MeC.contact_inputKey".bil_localized
	static let meMe_contact_keyEmpty = "MeC.contact_keyEmpty".bil_localized
	static let meMe_contact_noMore = "MeC.contact_noMore".bil_localized
	static let meMe_contact_recovered = "MeC.contact_recovered".bil_localized
	static let meMe_contact_contact = "MeC.contact_contact".bil_localized
	static let meMe_confirm = "MeC.confirm".bil_localized
	static let meMe_cancel = "MeC.cancel".bil_localized
	
	static let meWallet_deleteFailed = "MeC.wallet_deleteFailed".bil_localized
	static let meWallet_delete = "MeC.wallet_delete".bil_localized
	static let meWallet_deleteMessage = "MeC.wallet_deleteMessage".bil_localized
	static let meWallet_passwordEmpty = "MeC.wallet_passwordEmpty".bil_localized
	static let meWallet_passwordError = "MeC.wallet_passwordError".bil_localized
	static let meWallet_inputPassword = "MeC.wallet_inputPassword".bil_localized
	static let meWallet_tryLater = "MeC.wallet_tryLater".bil_localized
	static let meWallet_failed = "MeC.wallet_failed".bil_localized
	static let meWallet_backupNow = "MeC.wallet_backupNow".bil_localized
	
	static let meWalletAddress_addressAndBalance = "MeC.walletAddress_addressAndBalance".bil_localized
	
	static let meAboutUs_agreement = "MeC.aboutUs_agreement".bil_localized
	
	static let meAppSetting_sound = "MeC.appSetting_sound".bil_localized
	static let meAppSetting_currency = "MeC.appSetting_currency".bil_localized
	static let meAppSetting_cellSound = "MeC.appSetting_cellSound".bil_localized
	static let meAppSetting_cellCurrency = "MeC.appSetting_cellCurrency".bil_localized
	static let meAppSetting_saved = "MeC.appSetting_saved".bil_localized
}

extension String {
	static let newWallet_inputID_IKnow = "NewWalletC.inputID_IKnow".bil_localized
	static let newWallet_inputID_title = "NewWalletC.inputID_title".bil_localized
	static let newWallet_inputID_range = "NewWalletC.inputID_range".bil_localized
	static let newWallet_inputID_format = "NewWalletC.inputID_format".bil_localized
	static let newWallet_inputID_prefix = "NewWalletC.inputID_prefix".bil_localized
	static let newWallet_inputID_exits = "NewWalletC.inputID_exits".bil_localized
	
	static let newWallet_create_typeNew = "NewWalletC.create_typeNew".bil_localized
	static let newWallet_create_typeRecover = "NewWalletC.create_typeRecover".bil_localized
	static let newWallet_create_typeResetPassword = "NewWalletC.create_typeResetPassword".bil_localized
	static let newWallet_create_wallet = "NewWalletC.create_wallet".bil_localized
	static let newWallet_create_begin = "NewWalletC.create_begin".bil_localized
	static let newWallet_create_pwdInput = "NewWalletC.create_pwdInput".bil_localized
	static let newWallet_create_pwdRange = "NewWalletC.create_pwdRange".bil_localized
	static let newWallet_create_pwdNotEqual = "NewWalletC.create_pwdNotEqual".bil_localized
	static let newWallet_create_failed = "NewWalletC.create_failed".bil_localized
	static let newWallet_create_pwdEmpty = "NewWalletC.create_pwdEmpty".bil_localized
	static let newWallet_create_createPwd = "NewWalletC.create_createPwd".bil_localized
	static let newWallet_create_confirmPwd = "NewWalletC.create_confirmPwd".bil_localized
	static let newWallet_createSuccess_new = "NewWalletC.createSuccess_new".bil_localized
	static let newWallet_createSuccess_recover = "NewWalletC.createSuccess_recover".bil_localized
	static let newWallet_createSuccess_reset = "NewWalletC.createSuccess_reset".bil_localized
	static let newWallet_createSuccess = "NewWalletC.createSuccess".bil_localized
	static let newWallet_import_emptyTitle = "NewWalletC.import_emptyTitle".bil_localized
	static let newWallet_import_failed = "NewWalletC.import_failed".bil_localized
	static let newWallet_import_checkAgain = "NewWalletC.import_checkAgain".bil_localized
	static let newWallet_import_exits = "NewWalletC.import_exits".bil_localized
	static let newWallet_import_resetOrNot = "NewWalletC.import_resetOrNot".bil_localized
	static let newWallet_import_reset = "NewWalletC.import_reset".bil_localized

}

extension String {
	static let backupWallet_mnemonic_emptyTitle = "BackupWalletC.mnmonic_emptyTitle".bil_localized
	static let backupWallet_mnemonic_snapshotTip = "BackupWalletC.mnmonic_snapshotTip".bil_localized
	static let backupWallet_mnemonic_tryLater = "BackupWalletC.mnmonic_tryLater".bil_localized
	static let backupWallet_mnemonic_failed = "BackupWalletC.mnmonic_failed".bil_localized
	static let backupWallet_mnemonic_backupWallet = "BackupWalletC.mnmonic_backupWallet".bil_localized
	static let backupWallet_mnemonic_inputPwd = "BackupWalletC.mnmonic_inputPwd".bil_localized
	static let backupWallet_mnemonic_getWalletFailed = "BackupWalletC.mnmonic_getWalletFailed".bil_localized
	static let backupWallet_mnemonic_decodeWalletFailed = "BackupWalletC.mnmonic_decodeWalletFailed".bil_localized
	static let backupWallet_mnemonic_confirm = "BackupWalletC.mnmonic_confirm".bil_localized
	static let backupWallet_mnemonic_cancel = "BackupWalletC.mnmonic_cancel".bil_localized
	
	static let backupWallet_verify_emptyTitle = "BackupWalletC.verify_emptyTitle".bil_localized
}

extension String {
	static let contact_contact_addByID = "ContactC.contact_addByID".bil_localized
	static let contact_contact_addByAddress = "ContactC.contact_addByAddress".bil_localized
	static let contact_contact_scanCode = "ContactC.contact_scanCode".bil_localized
	static let contact_contact_cancel = "ContactC.contact_cancel".bil_localized
	static let contact_contact_addressExits = "ContactC.contact_addressExits".bil_localized
	static let contact_contact_addressInvalid = "ContactC.contact_addressInvalid".bil_localized
	static let contact_contact_IDExits = "ContactC.contact_IDExits".bil_localized
	
	static let contact_detail_walletID = "ContactC.detail_walletID".bil_localized
	static let contact_detail_walletAddress = "ContactC.detail_walletAddress".bil_localized
	static let contact_detail_id = "ContactC.detail_id".bil_localized
	static let contact_detail_address = "ContactC.detail_address".bil_localized
	static let contact_detail_error = "ContactC.detail_error".bil_localized
	
	static let contact_search_empty = "ContactC.search_empty".bil_localized
	static let contact_search_tooLong = "ContactC.search_tooLong".bil_localized
	
	static let contact_searchResult_name = "ContactC.searchResult_name".bil_localized
	static let contact_searchResult_remark = "ContactC.searchResult_remark".bil_localized
	static let contact_searchResult_input = "ContactC.searchResult_input".bil_localized
	static let contact_searchResult_surport = "ContactC.searchResult_surport".bil_localized
	static let contact_searchResult_wei = "ContactC.searchResult_wei".bil_localized
	static let contact_searchResult_nameEmpty = "ContactC.searchResult_nameEmpty".bil_localized
	static let contact_searchResult_success = "ContactC.searchResult_success".bil_localized
}

extension String {
    var bil_localized: String {
        get {
            guard let tableName = components(separatedBy: ".").first else {
                return self
            }
            
            return NSLocalizedString(self, tableName: tableName, comment: "")
        }
    }
}
